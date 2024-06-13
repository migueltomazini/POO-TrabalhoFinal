import socket
import shutil
import handleC

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    print(f"Listening to port {PORT}")
    conn, addr = s.accept()

    try:
        with conn:
            print(f"Connected by {addr}")
            while True:
                # receber os dados
                # caso seja vazia, então acabou o programa
                data = conn.recv(1024)
                if not data:
                    break

                # decodificar os dados recebidos para utf-8
                string = data.decode("utf-8")
                print("input: ", string)
                
                if (string.split(" ")[0] == "Carregar:"):
                    orig = "./arquivos/" + string.split(" ")[1]
                    dest = "./" + string.split(" ")[1]
                    shutil.copyfile(orig, dest)
                else:
                    for command in string.split("/"):
                        output = handleC.run_c_program(command)
                    if output:
                        conn.sendall(output.encode("utf-8"))
    except Exception as e:
        print(f"Exception occurred: {e}")
        s.close()
    finally:
        s.close()
