all:
	javac MainFrame.java Player.java PlayerDialog.java PlayerManager.java SearchPanel.java

run:
	java MainFrame

clean:
	rm *.class