document.getElementById('uploadForm').addEventListener('submit', (e) => {
    e.preventDefault();

    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    if (!file) return;

    const fileName = file.name;
    const lastDotIndex = fileName.lastIndexOf('.');
    const ext = lastDotIndex !== -1 ? fileName.substring(lastDotIndex) : '';

    const status = document.getElementById('status');
    const result = document.getElementById('result');
    const link = document.getElementById('downloadLink');

    status.innerHTML = 'Загрузка: <span id="progress">0%</span>';
    status.classList.remove('hidden');
    result.classList.add('hidden');

    const xhr = new XMLHttpRequest();

    xhr.upload.addEventListener('progress', (event) => {
        if (event.lengthComputable) {
            const percent = Math.round((event.loaded / event.total) * 100);
            document.getElementById('progress').textContent = `${percent}%`;
        }
    });

    xhr.addEventListener('load', () => {
        if (xhr.status >= 200 && xhr.status < 300) {
            const url = xhr.responseText;
            link.href = url;
            link.textContent = url;
            result.classList.remove('hidden');
            refreshStats();
        } else {
            alert('Ошибка: сервер вернул статус ' + xhr.status);
        }
        status.classList.add('hidden');
    });

    xhr.addEventListener('error', () => {
        alert('Ошибка сети при загрузке файла');
        status.classList.add('hidden');
    });

    xhr.open('POST', 'http://localhost:8080/upload');
    xhr.setRequestHeader('X-File-Extension', ext);
    xhr.send(file);
});

document.getElementById("copyButton").addEventListener('click', async () => {
    const link = document.getElementById("downloadLink");
    const url = link.href;

    if (!url) {
        alert('Нет ссылки для копирования');
        return;
    }

    try {
        await navigator.clipboard.writeText(url);
        alert('Ссылка скопирована в буфер обмена!');
    } catch (err) {
        console.error('Не удалось скопировать: ', err);
        alert('Не удалось скопировать ссылку. Попробуйте вручную.');
    }
});

function formatBytes(bytes) {
    if (!bytes || bytes <= 0) return '0 Б';
    const k = 1024;
    const sizes = ['Б', 'КБ', 'МБ', 'ГБ', 'ТБ'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

function formatTime(ms) {
    if (!ms || ms <= 0) return '-';
    try {
        return new Date(ms).toLocaleString();
    } catch (_) {
        return '-';
    }
}

async function refreshStats() {
    try {
        const res = await fetch('/stats');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        const data = await res.json();

        const totalFilesEl = document.getElementById('statTotalFiles');
        const totalSizeEl = document.getElementById('statTotalSize');
        const newestEl = document.getElementById('statNewest');
        const lastAccessEl = document.getElementById('statLastAccess');

        if (totalFilesEl) totalFilesEl.textContent = (data.fileCount ?? 0);
        if (totalSizeEl) totalSizeEl.textContent = formatBytes(data.totalSizeBytes || 0);
        if (newestEl) newestEl.textContent = formatTime(data.latestUploadTime);
        if (lastAccessEl) lastAccessEl.textContent = formatTime(data.latestAccessTime);
    } catch (e) {
        console.error('Не удалось получить статистику', e);
    }
}

const refreshBtn = document.getElementById('refreshStats');
if (refreshBtn) {
    refreshBtn.addEventListener('click', refreshStats);
}
refreshStats();
setInterval(refreshStats, 15000);