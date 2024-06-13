# funções necessárias do trabalho de arquivos:
# busca com id, idade, nomeJogador, nacionalidade, ou nomeClube
# alterar dados selecionados (guardar o id do selecionado, e ter os dados a alterar). Remover o id e inserir novamente.
# remoção e inserção no arquivo
# listagem de todos os jogadores

# em resumo:
# inserção, remoção, listagem, busca

import subprocess

def run_c_program(input_data, id_client):
    # Run the C program as a subprocess
    work_dir = "./" + str(id_client)
    process = subprocess.Popen(
        "../../arquivos/bin/programaTrab",  # Adjust the path to your compiled C program
        stdin=subprocess.PIPE,  # Set stdin to a pipe to communicate with the process
        stdout=subprocess.PIPE,  # Capture the output of the process
        stderr=subprocess.PIPE,  # Capture any error messages
        text=True,  # Use text mode for communication
        cwd=work_dir
    )
    
    # Provide input to the subprocess
    stdout, stderr = process.communicate(input_data)

    # Check for errors
    if process.returncode != 0:
        print("Error:", stderr)
    else:    
        return stdout