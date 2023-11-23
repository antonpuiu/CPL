ANTLR4=java -jar /usr/local/lib/antlr4.jar
CLASSPATH="/usr/local/lib/antlr4.jar:."

bin/Test.class: Test.java bin/CPLangParser.class bin/CPLangLexer.class
	javac -cp $(CLASSPATH) -d bin Test.java

bin/CPLangParser.class: CPLangParser.java
	javac -cp $(CLASSPATH) -d bin CPLangParser.java

bin/CPLangLexer.class: CPLangLexer.java
	javac -cp $(CLASSPATH) -d bin CPLangLexer.java

CPLangLexer.java: CPLangLexer.g4
	java -jar /usr/local/lib/antlr4.jar -visitor -listener CPLangLexer.g4

CPLangParser.java: CPLangParser.g4 CPLangLexer.java
	java -jar /usr/local/lib/antlr4.jar -visitor -listener CPLangParser.g4

clean:
	rm -rf bin/* CPLang*.java *.interp *.tokens

run: bin/Test.class
	java -cp bin:$(CLASSPATH) Test
