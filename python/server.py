import threading
import socket
import shutil
import handleC
import os

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 11111  # Port to listen on (non-privileged ports are > 1023)

def handle_client(conn, addr):
    print(f"Connected by {addr}")
    try:
        while True:
            # receber os dados
            # caso seja vazia, então acabou o programa
            data = conn.recv(1024)
            if not data:
                break

            # decodificar os dados recebidos para utf-8
            string = data.decode("utf-8")
            print("input: ", repr(string))

            # Carregar fifa2017.bin
            if (string.split(" ")[0] == "Carregar:"):
                orig = "./arquivos/" + string.split(" ")[1]
                
                os.makedirs("./" + str(addr[1]), exist_ok=True)
                dest = "./" + str(addr[1]) + "/" + string.split(" ")[1]
                
                shutil.copyfile(orig, dest)
            else:
                for command in string.split("/"):
                    output = handleC.run_c_program(command, addr[1])
                if output:
                    conn.sendall(output.encode("utf-8"))
    except Exception as e:
        print(f"Exception occurred: {e}")
    finally:
        conn.close()

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    print(f"Listening to port {PORT}")
    while True:
        conn, addr = s.accept()
        client_thread = threading.Thread(
            target=handle_client, args=(conn, addr))
        client_thread.start()
