import socket
import handleC

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 12345  # Port to listen on (non-privileged ports are > 1023)

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
                # transformar em uma lista
                string = data.decode("utf-8")
                output = handleC.run_c_program(string)
                print(output)

                # pular a string vazia (que possui apenas um '\n')
                conn.recv(1024)
                
                # Send the response back to the client
                conn.sendall(output.encode("utf-8"))
    except Exception as e:
        print(f"Exception occurred: {e}")
    finally:
        s.close()

