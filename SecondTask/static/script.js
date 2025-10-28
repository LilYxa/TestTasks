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