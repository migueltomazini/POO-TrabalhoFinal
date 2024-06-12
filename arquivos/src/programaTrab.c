// Este é um trabalho feito pelos alunos:
// Pietra Gullo Salgado Chaves - 14603822
// Shogo Shima - 12675145

#include "busca.h"
#include "escrita.h"
#include "funcoes_fornecidas.h"
#include "indice.h"
#include "insercao.h"
#include "leitura.h"
#include "remocao.h"

int main(void) {
    /*
    funcionalidades:
    [1]: CREATE TABLE - Cria o arquivo em binário que possiblita a manipulação do csv
    [2]: SELECT - Lista todos os dados de um arquivo em binário
    [3]: WHERE - Critério de busca
    [4]: CREATE INDEX - Cria o arquivo de índices
    [5]: DELETE - Remover dados de uma tabela
    [6]: INSERT INTO - Inserir dados em uma tabela
    */
    int func;
    scanf("%d", &func);
    if (func == 1) {
        char *nomeCsv = (char *)malloc(1024 * sizeof(char));
        char *nomeBin = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeCsv);
        scanf(" %s", nomeBin);
        CREATE_TABLE(nomeCsv, nomeBin);
        binarioNaTela(nomeBin);
        free(nomeBin);
        free(nomeCsv);
    } else if (func == 2) {
        char *nomeBin = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeBin);
        SELECT(nomeBin);
        free(nomeBin);
    } else if (func == 3) {
        char *nomeBin = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeBin);
        WHERE(nomeBin);
        free(nomeBin);
    } else if (func == 4) {
        char *nomeArquivoBinario = (char *)malloc(1024 * sizeof(char));
        char *nomeArquivoIndice = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeArquivoBinario);
        scanf(" %s", nomeArquivoIndice);
        if (CREATE_INDEX(nomeArquivoBinario, nomeArquivoIndice))
            binarioNaTela(nomeArquivoIndice);
        free(nomeArquivoBinario);
        free(nomeArquivoIndice);
    } else if (func == 5) {
        char *nomeArquivoBinario = (char *)malloc(1024 * sizeof(char));
        char *nomeArquivoIndice = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeArquivoBinario);
        scanf(" %s", nomeArquivoIndice);
        DELETE(nomeArquivoBinario, nomeArquivoIndice);
        free(nomeArquivoBinario);
        free(nomeArquivoIndice);
    } else if (func == 6) {
        char *nomeArquivoBinario = (char *)malloc(1024 * sizeof(char));
        char *nomeArquivoIndice = (char *)malloc(1024 * sizeof(char));
        scanf(" %s", nomeArquivoBinario);
        scanf(" %s", nomeArquivoIndice);
        INSERT_INTO(nomeArquivoBinario, nomeArquivoIndice);
        free(nomeArquivoBinario);
        free(nomeArquivoIndice);
    }
    return 0;
}