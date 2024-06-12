# funções necessárias do trabalho de arquivos:
# busca com id, idade, nomeJogador, nacionalidade, ou nomeClube
# alterar dados selecionados (guardar o id do selecionado, e ter os dados a alterar). Remover o id e inserir novamente.
# remoção e inserção no arquivo
# listagem de todos os jogadores

# em resumo:
# inserção, remoção, listagem, busca

import subprocess


def run_c_program(input_data):
    # Run the C program as a subprocess
    process = subprocess.Popen(
        "../arquivos/bin/programaTrab",  # Adjust the path to your compiled C program
        stdin=subprocess.PIPE,  # Set stdin to a pipe to communicate with the process
        stdout=subprocess.PIPE,  # Capture the output of the process
        stderr=subprocess.PIPE,  # Capture any error messages
        text=True  # Use text mode for communication
    )

    # Provide input to the subprocess
    stdout, stderr = process.communicate(input_data)

    # Check for errors
    if process.returncode != 0:
        print("Error:", stderr)
    else:    
        return stdout


# Example usage
# input_data = """
# 3 binario6.bin 3
# 2 nomeClube "INTER" nacionalidade "ITALY"
# 2 idade 22 nacionalidade "FRANCE"
# 3 idade 27 nacionalidade "GERMANY" nomeClube "FC BAYERN MUNCHEN"
# """
# output_data = run_c_program(input_data)
# print(output_data)