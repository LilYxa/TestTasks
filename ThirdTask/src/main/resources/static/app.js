async function loadWeather() {
    const city = document.getElementById('cityInput').value.trim();
    const errorDiv = document.getElementById('error');
    const canvas = document.getElementById('chartCanvas');

    if (!city) {
        errorDiv.textContent = 'Введите город';
        return;
    }

    errorDiv.textContent = '';
    const url = `/weather?city=${encodeURIComponent(city)}`;

    try {
        const response = await fetch(url);
        if (response.ok) {
            const data = await response.json();
            renderChart(canvas, data);
        } else {
            const text = await response.text();
            errorDiv.textContent = `Error: ${text}`;
        }
    } catch (err) {
        errorDiv.textContent = 'Сетевая ошибка: ' + err.message;
    }
}

document.getElementById('cityInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') loadWeather();
});

let chartInstance = null;

function renderChart(canvas, weatherJson) {
    try {
        const hourly = weatherJson?.hourly || {};
        const times = Array.isArray(hourly.time) ? hourly.time : [];
        const temps = Array.isArray(hourly.temperature_2m) ? hourly.temperature_2m : [];

        const n = Math.min(24, Math.min(times.length, temps.length));
        const labels = times.slice(0, n).map(t => formatHourLabel(t));
        const values = temps.slice(0, n);

        const ctx = canvas.getContext('2d');
        if (chartInstance) {
            chartInstance.destroy();
        }

        chartInstance = new Chart(ctx, {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    label: 'Температура (°C)',
                    data: values,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.2,
                    pointRadius: 2,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true },
                    title: { display: true, text: 'Прогноз на ближайшие 24 часа' }
                },
                scales: {
                    x: { title: { display: true, text: 'Время' } },
                    y: { title: { display: true, text: '°C' } }
                }
            }
        });
    } catch (e) {
        const errorDiv = document.getElementById('error');
        errorDiv.textContent = 'Ошибка построения графика: ' + e.message;
    }
}

function formatHourLabel(isoStr) {
    try {
        const d = new Date(isoStr);
        if (isNaN(+d)) return isoStr;
        const hh = String(d.getHours()).padStart(2, '0');
        const mm = String(d.getMinutes()).padStart(2, '0');
        return `${hh}:${mm}`;
    } catch (_) {
        return isoStr;
    }
}


