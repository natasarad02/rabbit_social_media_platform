// static/script.js
var map = L.map('mapid').setView([45.267136, 19.833549], 13);
var marker; // To store the single marker

// Add a tile layer (OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Function to update input fields and marker on map click
map.on('click', function(e) {
    document.getElementById('latitude').value = e.latlng.lat.toFixed(6);
    document.getElementById('longitude').value = e.latlng.lng.toFixed(6);

    if (marker) {
        map.removeLayer(marker); // Remove existing marker
    }
    marker = L.marker(e.latlng).addTo(map)
        .bindPopup("Location: " + e.latlng.lat.toFixed(4) + ", " + e.latlng.lng.toFixed(4))
        .openPopup();
});

// Function to send data to Flask backend
async function sendLocation() {
    const name = document.getElementById('name').value;
    const latitude = parseFloat(document.getElementById('latitude').value);
    const longitude = parseFloat(document.getElementById('longitude').value);
    const statusDiv = document.getElementById('message-status');

    if (isNaN(latitude) || isNaN(longitude)) {
        statusDiv.className = 'error';
        statusDiv.textContent = 'Please click on the map to select a location.';
        return;
    }

    try {
        const response = await fetch('/send_location', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, latitude, longitude })
        });

        const result = await response.json();

        if (result.status === 'success') {
            statusDiv.className = 'success';
            statusDiv.textContent = `Success! Message sent for ID: ${result.id}.`;
            // Clear inputs after successful send (optional)
            document.getElementById('name').value = 'Test Rabbit Shelter';
            document.getElementById('latitude').value = '';
            document.getElementById('longitude').value = '';
            if (marker) {
                map.removeLayer(marker);
                marker = null;
            }
        } else {
            statusDiv.className = 'error';
            statusDiv.textContent = `Error: ${result.message}`;
        }
    } catch (error) {
        console.error('Error sending location:', error);
        statusDiv.className = 'error';
        statusDiv.textContent = `An unexpected error occurred: ${error.message}`;
    }
}