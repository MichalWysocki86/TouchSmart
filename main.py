import mouse
import socket
import threading

PORT = 5050
SERVER = socket.gethostbyname(socket.gethostname())
server_address = (SERVER, PORT)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(server_address)


def handle_client(connection, address):
    print(f"CONNECTION FROM: {address}")

    connected = True
    while connected:
        data = connection.recv(11).decode('utf-8')
        if len(data):
            print(f"RECEIVED DATA: {data}")
            if data == 'LEFT_CLICK ':
                mouse.click('left')
                print('lewy klik')
            elif data == 'RIGHT_CLICK':
                mouse.click('right')
                print('prawy klik')
            elif data == 'END        ':
                connected = False
            else:
                coords = data.split(',')
                coord_x = int(coords[0])
                coord_y = int(coords[1])
                mouse.move(coord_x, coord_y, absolute=False, duration=0.1)
                print(coord_x, coord_y)

    connection.close()


def start():
    sock.listen()
    print(f"STARTING SERVER ON: {SERVER}")
    while True:
        connection, address = sock.accept()
        thread = threading.Thread(target=handle_client, args=(connection, address))
        thread.start()
        print(f"ACTIVE CONNECTIONS: {threading.active_count()-1}")


start()
