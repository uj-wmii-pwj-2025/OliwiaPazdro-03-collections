package uj.wmii.pwj.collections;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class BrainfuckInterpreter implements  Brainfuck{
    private final String program;
    private final PrintStream out;
    private final InputStream in;
    private final byte[] memoryCells;
    private int pointer;

    BrainfuckInterpreter(String program, PrintStream out, InputStream in, int stackSize) {
        if(program == null)
            throw new IllegalArgumentException("Program cannot be null.");
        if(program.isEmpty())
            throw new IllegalArgumentException("Program cannot be empty.");
        if(out == null)
            throw new IllegalArgumentException("Out cannot be null.");
        if(in == null)
            throw new IllegalArgumentException("In cannot be null.");
        if(stackSize < 1)
            throw new IllegalArgumentException("StackSize cannot be below 1.");

        this.program = program;
        this.out = out;
        this.in = in;
        memoryCells = new byte[stackSize];
        pointer = 0;
    }

    @Override
    public void execute() {
        int instruction_pointer = 0, brackets = 0;
        int programLength = program.length();
        char command = '\0';

        while(instruction_pointer < programLength) {
            command = program.charAt(instruction_pointer);

            switch (command) {
                case '>':
                    pointer++;
                    break;

                case '<':
                    pointer--;
                    break;

                case '+':
                    memoryCells[pointer]++;
                    break;

                case '-':
                    memoryCells[pointer]--;
                    break;

                case '.':
                    out.append((char)memoryCells[pointer]);
                    break;

                case ',':
                    try {
                        memoryCells[pointer] = (byte)in.read();
                    } catch (IOException e) {
                        throw new RuntimeException("read() error", e);
                    }
                    break;

                case '[':
                    if(memoryCells[pointer] == 0) {
                        brackets = 0;
                        while( instruction_pointer < programLength ) {
                            if (program.charAt(instruction_pointer) == '[') {
                                brackets ++;
                            } else if(program.charAt(instruction_pointer) == ']') {
                                brackets --;
                            }
                            if(brackets == 0)
                                break;
                            instruction_pointer++;
                        }
                    }
                    break;

                case ']':
                    if(memoryCells[pointer] != 0) {
                        brackets = 0;
                        while(instruction_pointer >= 0 ) {
                            if(program.charAt(instruction_pointer) == ']') {
                                brackets ++;
                            } if(program.charAt(instruction_pointer) == '[') {
                                brackets --;
                            }
                            if(brackets == 0)
                                break;
                            instruction_pointer--;
                        }
                    }
                    break;
            }
            instruction_pointer++;
        }
    }
}
