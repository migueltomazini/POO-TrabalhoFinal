import threading
import socket
import shutil
import handleC
import os

HOST = "127.0.0.1"  # Endereço padrão de interface de loopback (localhost)
PORT = 11111  # Porta para escutar (portas não privilegiadas são > 1023)


def handle_client(conn, addr):
    """
    Lida com a conexão do cliente. Recebe comandos, processa-os e envia as respostas apropriadas.
    """
    print(f"Connected by {addr}")
    try:
        while True:
            # receber os dados
            data = conn.recv(1024)
            if not data:
                 # Se não houver dados, a conexão foi encerrada
                break

            # decodificar os dados recebidos para utf-8
            string = data.decode("utf-8")
            print("input: ", repr(string))

            # Carregar fifa2017.bin
            if (string.split(" ")[0] == "Carregar:"):
                orig = "./arquivos/" + string.split(" ")[1]

                # Criar diretório para o cliente, se não existir
                os.makedirs("./" + str(addr[1]), exist_ok=True)
                dest = "./" + str(addr[1]) + "/" + string.split(" ")[1]

                # Copiar o arquivo para o diretório do cliente
                shutil.copyfile(orig, dest)

                handleC.run_make_commands('../arquivos')
            else:
                for command in string.split("/"):
                    # Executar o programa C com o comando recebido
                    output = handleC.run_c_program(command, addr[1])
                if output:
                    # Enviar a saída do programa de volta ao cliente
                    conn.sendall(output.encode("utf-8"))
        
        # Apagar o diretório criado para o cliente
        shutil.rmtree('./' + str(addr[1]))
    except Exception as e:
        print(f"Exception occurred: {e}")
    finally:
        conn.close()

# Configurar o servidor de socket
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    print(f"Listening to port {PORT}")
    while True:
        conn, addr = s.accept()
        client_thread = threading.Thread(
            target=handle_client, args=(conn, addr))
        client_thread.start()
