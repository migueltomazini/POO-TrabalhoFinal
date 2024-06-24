# funções necessárias do trabalho de arquivos:
# busca com id, idade, nomeJogador, nacionalidade, ou nomeClube
# alterar dados selecionados (guardar o id do selecionado, e ter os dados a alterar). Remover o id e inserir novamente.
# remoção e inserção no arquivo
# listagem de todos os jogadores

import subprocess
import os
import platform

def check_os():
    """
    Verifica o sistema operacional atual.
    Retorna 'Windows' se o SO for Windows, 'Linux' se for Linux, 
    e 'Unknown' para outros sistemas.
    """
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
    """
    Executa os comandos 'make clean' e 'make all' no diretório especificado.
    Lança uma exceção se ocorrer um erro em qualquer um dos comandos.
    """
    # Executa o comando `make clean
    process_clean = subprocess.run(
        ["make", "clean"],
        cwd=directory,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )

    # Verifica se houve erro ao executar `make clean`
    if process_clean.returncode != 0:
        raise Exception(f"Error running 'make clean': {process_clean.stderr}")
        return

    # Executa o comando `make all`
    process_all = subprocess.run(
        ["make", "all"],
        cwd=directory,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )

    # Verifica se houve erro ao executar `make all`
    if process_all.returncode != 0:
        raise Exception(f"Error running 'make all': {process_all.stderr}")
        return


def run_c_program(input_data, id_client):
    """
    Executa um programa C com os dados de entrada fornecidos.
    Determina o sistema operacional atual e executa o programa C 
    correspondente. Retorna a saída do programa ou imprime erros, se houver.
    """
    current_os = check_os()
    work_dir = os.path.join(".", str(id_client))
    
    if current_os == 'Windows':
        executable_path = "../../arquivos/bin/programaTrab.exe"
    elif current_os == 'Linux':
        executable_path = "../../arquivos/bin/programaTrab"
    else:
        raise Exception("Unsupported Operating System")
    
    process = subprocess.Popen(
        executable_path,  # Caminho para o programa C compilado
        stdin=subprocess.PIPE,  # Define stdin como pipe para comunicação com o processo
        stdout=subprocess.PIPE,  # Captura a saída do processo
        stderr=subprocess.PIPE,  # Captura mensagens de erro
        text=True,  # Usa modo texto para comunicação
        cwd=work_dir
    )
    
    # Fornece dados de entrada para o subprocesso
    stdout, stderr = process.communicate(input_data)
    print(stdout)

     # Verifica se houve erros
    if process.returncode != 0:
        print("Error:", stderr)
    else:    
        return stdout
