# MIPS-Simulator
An EECS 2021 project, meant to simulate a basic subset of MIPS instructions. Input for this program is a text file (.txt) that includes
two hexadecimal numbers separated by a space ("\ "). The first of which is a memory address of the instruction (used for jumps), and the
second is the instruction. Each line is read one after another and changes contents of the 32 MIPS registers until the MIPS program inputted ends.

The following subset of instructions are simulated:
+ SLL   (Shift Left Logical)
+ SRL   (Shift Right Logical)
+ ADDI  (Add Immediate)
+ ADD   (Add)
+ AND   (Logical AND)
+ SLT   (Set on Less Than)
+ SLTU  (Set on Less Than Unsigned)
+ BEQ   (Branch on Equal)
+ BNE   (Branch on Not Equal)
+ NOR   (Logical NOR)

[See this link for more information on MIPS instructions](http://www.mrc.uidaho.edu/mrc/people/jff/digital/MIPSir.html)

Due to specifications, all code must have been contained in one file and registers would be outputted in a certain format.
