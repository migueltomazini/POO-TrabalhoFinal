C_FLAGS=-std=c99 -Wall -Werror -Iinclude

all: bin/funcoes_fornecidas.o bin/escrita.o bin/leitura.o bin/busca.o bin/lista.o bin/indice.o bin/remocao.o bin/insercao.o bin/programaTrab.o
	gcc -g bin/*.o -o bin/programaTrab ${C_FLAGS}

bin/funcoes_fornecidas.o:
	mkdir -p bin/
	gcc -g -c src/funcoes_fornecidas.c -o bin/funcoes_fornecidas.o -Iinclude 

bin/busca.o:
	gcc -g -c src/busca.c -o bin/busca.o -Iinclude 

bin/escrita.o:
	gcc -g -c src/escrita.c -o bin/escrita.o -Iinclude 

bin/leitura.o:
	gcc -g -c src/leitura.c -o bin/leitura.o -Iinclude 

bin/lista.o:
	gcc -g -c src/lista.c -o bin/lista.o -Iinclude

bin/indice.o:
	gcc -g -c src/indice.c -o bin/indice.o -Iinclude 

bin/remocao.o:
	gcc -g -c src/remocao.c -o bin/remocao.o -Iinclude  

bin/insercao.o:
	gcc -g -c src/insercao.c -o bin/insercao.o -Iinclude 

bin/programaTrab.o:
	gcc -g -c src/programaTrab.c -o bin/programaTrab.o -Iinclude

clean:
	rm bin/*

run:
	./bin/programaTrab

debug:
	valgrind --leak-check=yes --log-file=valgrind.rpt ./bin/programaTrab
	
zip:
	zip -r main.zip include src makefile

shared:
	gcc -shared -o sharedLibrary.so -fPIC src/funcoes_fornecidas.c src/busca.c src/escrita.c src/leitura.c src/lista.c src/indice.c src/remocao.c src/insercao.c src/programaTrab.c