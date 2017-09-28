# BrainPack

BrainPack is a small utility which converts Brainfuck source code into a binary form. I make use of this tool in my
[homebrew CPU project](https://hackaday.io/project/4237-mental-1-a-brainfuck-cpu), which uses Brainfuck as its native
instruction set.

## Building

Right now, I'm just building this as an IntelliJ project. Feel free to send me your more portable build instructions :)

## Running

This program requires four arguments:

 * `source` Brainfuck file to be parsed
 * `mapping` A file that defines a mapping from Brainfuck instructions to their encoded form
 * `filesize` Size of the file to be generated
 * `output` The output file, which will have the encoded program written to it

The mapping file has a strict format.

 * The first line of the file is a decimal number indicating the bit width of the encoded instruction
 * All subsequent lines have three parts:
   * The first character is the the character to be encoded
   * Next, there should be one or more spaces
   * Lastly, the encoded form of the instruction is given as a decimal number

As a quick demonstration, a sample program and mapping are given in the `example/` directory. After compilation, run
this with `java -jar brainpack.jar ./example/example.bf ./example/example.bfmap 8192 ./example/example.out`.