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
    print(os_name, platform_system)

    if os_name == 'posix' and platform_system.split('_')[0] == 'CYGWIN':
        return 'Windows'
    elif os_name == 'posix' and platform_system == 'Linux':
        return 'Linux'
    else:
        return 'Unknown'
    

def run_make_commands(directory):
    # Run `make clean`
    process_clean = subprocess.run(
        ["make", "clean"],
        cwd=directory,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )

    # Check for errors in `make clean`
    if process_clean.returncode != 0:
        raise Exception(f"Error running 'make clean': {process_clean.stderr}")
        return

    # Run `make all`
    process_all = subprocess.run(
        ["make", "all"],
        cwd=directory,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )

    # Check for errors in `make all`
    if process_all.returncode != 0:
        raise Exception(f"Error running 'make all': {process_all.stderr}")
        return


def run_c_program(input_data, id_client):
    current_os = check_os()
    work_dir = os.path.join(".", str(id_client))
    
    if current_os == 'Windows':
        executable_path = "../../arquivos/bin/programaTrab.exe"
    elif current_os == 'Linux':
        executable_path = "../../arquivos/bin/programaTrab"
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