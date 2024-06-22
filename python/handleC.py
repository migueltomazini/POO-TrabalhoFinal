# funções necessárias do trabalho de arquivos:
# busca com id, idade, nomeJogador, nacionalidade, ou nomeClube
# alterar dados selecionados (guardar o id do selecionado, e ter os dados a alterar). Remover o id e inserir novamente.
# remoção e inserção no arquivo
# listagem de todos os jogadores

# em resumo:
# inserção, remoção, listagem, busca

import subprocess
import os
import platform

def check_os():
    os_name = os.name
    platform_system = platform.system()

    if os_name == 'nt' or platform_system == 'Windows':
        return 'Windows'
    elif os_name == 'posix' and platform_system == 'Linux':
        return 'Linux'
    else:
        return 'Unknown'

def run_c_program(input_data, id_client):
    current_os = check_os()
    work_dir = os.path.join(".", str(id_client))
    
    if current_os == 'Windows':
        executable_path = r"..\arquivos\bin\windows\programaTrab.exe"
    elif current_os == 'Linux':
        executable_path = "../../arquivos/bin/linux/programaTrab"
    else:
        raise Exception("Unsupported Operating System")
    
    process = subprocess.Popen(
        executable_path,  # Adjust the path to your compiled C program
        stdin=subprocess.PIPE,  # Set stdin to a pipe to communicate with the process
        stdout=subprocess.PIPE,  # Capture the output of the process
        stderr=subprocess.PIPE,  # Capture any error messages
        text=True,  # Use text mode for communication
        cwd=work_dir
    )
    
    # Provide input to the subprocess
    stdout, stderr = process.communicate(input_data)
    print(stdout)

    # Check for errors
    if process.returncode != 0:
        print("Error:", stderr)
    else:    
        return stdout