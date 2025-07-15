# app.py
from flask import Flask, render_template, request, jsonify
import pika
import json
import uuid 

app = Flask(__name__)

# --- RabbitMQ Konfiguracija ---
RABBITMQ_HOST = 'localhost'
RABBITMQ_PORT = 5672
RABBITMQ_USER = 'guest'
RABBITMQ_PASS = 'guest'

EXCHANGE_NAME = 'rabbit.location.exchange' 
ROUTING_KEY = 'location.update.key'     


def send_message_to_rabbitmq(message_data):
    try:
        credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=RABBITMQ_HOST, port=RABBITMQ_PORT, credentials=credentials))
        channel = connection.channel()

        # Deklarisanje Exchange-a (isto kao u Spring Boot-u)
        channel.exchange_declare(exchange=EXCHANGE_NAME, exchange_type='direct', durable=True)

        # JSON serijalizacija poruke
        message_json = json.dumps(message_data)

        channel.basic_publish(
            exchange=EXCHANGE_NAME,
            routing_key=ROUTING_KEY,
            body=message_json,
            properties=pika.BasicProperties(
                delivery_mode=2,  # make message persistent
            ))
        print(f" [x] Sent '{message_json}' to exchange '{EXCHANGE_NAME}' with routing key '{ROUTING_KEY}'")
        connection.close()
        return True
    except Exception as e:
        print(f" [!] Error sending message to RabbitMQ: {e}")
        return False

# --- Flask Rutiranje ---

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/send_location', methods=['POST'])
def send_location():
    data = request.get_json()
    
    # Validacija i priprema podataka
    location_id = str(uuid.uuid4()) # Generise jedinstveni ID
    location_name = data.get('name', 'Nepoznat Azil')
    latitude = data.get('latitude')
    longitude = data.get('longitude')

    if latitude is None or longitude is None:
        return jsonify({"status": "error", "message": "Latitude and longitude are required"}), 400

    # Kreiranje poruke u formatu ocekivanom od Spring Boot potrosaca
    message_to_send = {
        "id": location_id,
        "name": location_name,
        "locationData": { # Ime polja mora biti "locationData" kao u DTO
            "latitude": latitude,
            "longitude": longitude
        }
    }

    if send_message_to_rabbitmq(message_to_send):
        return jsonify({"status": "success", "message": "Location sent successfully!", "id": location_id}), 200
    else:
        return jsonify({"status": "error", "message": "Failed to send location to RabbitMQ"}), 500

if __name__ == '__main__':
    app.run(debug=True, port=8081) # Pokrenuti na drugom portu, npr. 8081