import ctypes
import socket
import os

# from pathlib import Path
if os.name == "posix":
    path = './clibrary.so'
elif os.name == 'nt':
    path = './clibrary.dll'

clibrary = ctypes.CDLL(str(path))

def start_server():
    clibrary.display()

    # Cria um objeto socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Definição do host e porta
    host = '127.0.0.1'
    port = 31415

    # Busca por conexões
    server_socket.listen()
    print("Servidor está aguardando conexões...")

    # Conexão encontrada
    conn, addr = server_socket.accept()
    with conn:
        print(f"Conectado por {addr}")
        while True:
            data = conn.recv(1024)
            if not data:
                break
            print(f"Recebido: {data.decode()}")
            conn.sendall(data)

if __name__ == "__main__":
    start_server()