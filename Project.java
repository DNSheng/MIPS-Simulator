import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Note: For proper operation, the input file fed into this program must:
 * 			- Be in the format of the example(s) posted
 * 			- Already have the address sorted from lowest to highest (not in scrambled order)
 * 			- Have less than or equal to 100 instructions
 * 					- This can be modified by changing 100 to whatever amount is needed
 * 					  in the <code>instructionAmount</code> variable here, "for-loop" here,
 * 					  <code>input[]</code> array in <code>Registers</code>, and the 
 * 					  <code>createInput()</code> method in <code>Registers</code>.
 * 			- To do read a custom file, the command line argument must be passed. For example,
 * 			  "java project C:/Users/Dan/Desktop/MIPS.txt".
 * 
 * This is the main class run to run the entire program. It takes a file for an input
 * formatted like the example posted on the web site and will read each address and instruction.
 * The address and instruction will be stored internally (Not output. To see it, just
 * un-comment the block in the UI at the bottom). After reading everything given, it will go 
 * through each instruction and execute them. Each instruction will update the 
 * <code>register[]</code> and once all the instructions have been executed, the final register 
 * values are output.
 * 
 * The operations that this program can do are: SLL, SRL, ADDI, ADD, AND, SLT, SLTU, BEQ, BNE, and NOR.
 * If given an operation that is not any of the above, the program will print "Illegal Instruction!"
 * and exit immediately.
 * 
 * ALSO: Some JavaDoc for the methods will reference classes. Originally, there were 5 classes 
 * used in the program, each with a specific function. Since the submission requires one class 
 * called "project", I had to copy and paste the methods from each class into here. I was too lazy to
 * fix the needed changes to JavaDoc. I only got rid of references to other classes when methods were
 * used (Ex. Breakdown.breakdown()). And all the comments aren't lined up either.
 * 
 * @author Dan Sheng, 213587712, York University
 *
 */
public class Project
{
	public static void main(String[] args) throws IOException, NumberFormatException
	{
		String line = null;
		String fileName = args[0];																		// Takes the command line argument
		int instructionAmount = 0;
		createRegister();																				// Creation of register and input arrays
		createInput();
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);								// Reads the file
			while((line = bufferedReader.readLine()) != null)											// If there is a line,
			{
				String[] splited = line.split("\\s+");													// Split it based on any space
				int size = splited.length;																// Finds the entries in the splited[] array
				for(int i = 0; i < size; i++)
				{
					if(i == 0)																			// This grabs the first entry in the text (address)
					{
						if(splited[i].equals(""))														// I placed this first to deal with the bug where an
						{																				// extra empty space appears at the end. This makes
						}																				// it so it does nothing to the program.
						else
						{
							String address = HexToBinary(splited[i]);
							int intAddress = Integer.parseInt(address, 2);								// Converts the address from hex to an integer.
							if(originalAddress == 0)													// If the originalAddress is empty (i.e. beginning
							{																			// of program therefore first address entry),
								originalAddress = intAddress;											// set the originalAddress to that value
								currentAddress = 0;														// Set the currentAddress to zero (therefore input[0])
							}
							else																		// If it's not the first, change the current address
							{																			// to the address X many times after the first.
								currentAddress = (intAddress - originalAddress)/4;
							}																			// This makes it to be input[1], input[2], etc.
						}																				// Division by 4 because MIPS words take 4 bytes and
					}																					// so address increases in increments of 4.
					else if(i == 1)																		// When i == 1, it is the instruction part
					{
						String instruction = HexToBinary(splited[i]);
						int intInstruction = Integer.parseInt(instruction, 2);							// Converts the instruction from binary to decimal for
						input[currentAddress] = intInstruction; 										// storage in input[], then stores it in input[X]
					}																					// where X is the currentAddress of the address it's
				}																						// related to.
			}
			bufferedReader.close();																		// Closes the reader
		}
		catch(IOException e)																			// Catches exceptions
		{
			System.out.println("File not found");
		}
		catch(NumberFormatException e2)
		{
			System.out.println("");
		}
		
		while(instructionAmount < 100)																	// Cycles through all 100 arrays in the input[]
		{																								// For each entry, converts the instruction from decimal
			String instruction = DecimalToBinary(input[instructionAmount]);								// to binary, then sends it to breakDown() to perform the MIPS operation
			breakDown(instruction);	
			if(jump == true)																			// If the instruction is BEQ or BNE, then jump == true
			{
				instructionAmount = instructionAmount + jumpAddress;									// In this case, the next entry of input[] taken jumps to the specified point
				jump = false;																			// Reset jump back to false so it doesn't loop and prepares for next jump
			}
			else
			{
				instructionAmount++;																	// If no jump, the instruction executes and it goes to the next instruction in input[]
			}
		}
		
		/*System.out.println("Address\t\tInstruction");													// UNCOMMENT THIS TO DISPLAY THE INPUT[] IN THE OUTPUT WINDOW
		for(int i = 0; i < 100; i++)
		{
			System.out.println("[" + i + "]\t\t" + Registers.input[i]);
		}*/
		
		for(int counter = 0; counter < 8; counter++)													// This is sloppily written and doesn't even look good when printed.
		{																								// I just have this output because it says so on the lab. It's easier if you
			if(counter == 0)																			// comment this out and use the one right above.
			{
				System.out.println("[" + (counter) + "]" + 
						registers[(counter)] + "\t  " + "[" + (counter + 1)
						+ "]" + registers[(counter + 1)] + "\t  "
						+ "[" + (counter + 2) + "]" + registers[(counter + 2)]
						+ "\t  " + "[" + (counter + 3) + "]" + registers[(counter + 3)]);
			}
			else
			{
				System.out.println("[" + (4 * counter) + "]" + 
						registers[(4 * counter)] + "\t  " + "[" + (4 * counter + 1)
						+ "]" + registers[(4* counter + 1)] + "\t  "
						+ "[" + (4 * counter + 2) + "]" + registers[(4 * counter + 2)]
						+ "\t  " + "[" + (4 * counter + 3) + "]" + registers[(4 * counter + 3)]);
			}
		}
	}
	
	/**
	 * A static class that converts a hex number to a binary number.
	 * @param Hex The hex string that will be converted to binary.
	 * @return The converted binary string with zero padding.
	 */
	public static String HexToBinary(String Hex)
	{
		int i = Integer.parseInt(Hex, 16);
		String binary = Integer.toBinaryString(i);			// Converts hex to binary
		StringBuffer binary2 = new StringBuffer(binary);	// Makes a binary string that can receive zero padding
		int binaryLength = binary.length();					// Finds length of binary string
		int zeroPad = 32 - binaryLength;					// Finds the needed amount of zero padding
		StringBuffer frontZero = new StringBuffer();		// String that will be zero padding
		for(int a = 0; a < zeroPad; a++)					// Adds "0" to frontZero depending on how much is needed
		{
			frontZero.append("0");
		}
		String frontZero2 = frontZero.toString();			// Converts frontZero to string to prepare for return
		String finalBinary = binary2.toString();			// Converts binary2 to string to prepare for return
		return frontZero2 + finalBinary;					// Returns hex in binary with zero padding 
	}
	
	/**
	 * A static class that converts a decimal number to a binary number.
	 * This is needed because it will take the instructions from the <code>input[]</code>
	 * array which are in decimal form and convert them to binary. A simple
	 * <code>Integer.toBinaryString()</code> is not applicable because a 32-bit 
	 * binary number with zero padding is necessary for both the <code>Breakdown</code> class
	 * and <code>breakDown</code> method, and is also just how MIPS functions.
	 * 
	 * @param decimal Must be an integer.
	 * @return The decimal number as a 32-bit binary number.
	 */
	public static String DecimalToBinary(int decimal)
	{
		String binaryFromDecimal = Integer.toBinaryString(decimal);					// Takes the input in decimal and converts to a binary string
		StringBuffer binaryFromDecimal2 = new StringBuffer(binaryFromDecimal);		// Turns that string to a string buffer where zeroes can be added
		int zeroPad = 32 - binaryFromDecimal.length();								// Finds amount of zeroes in front needed
		StringBuffer frontZero = new StringBuffer();								// Makes a string buffer to store said zeroes
		for(int length = 0; length < zeroPad; length++)
		{
			frontZero.append("0");													// Adds the zeroes to the string buffer
		}
		String frontZero2 = frontZero.toString();									// Converts to string to ready for output
		String finalBinary = binaryFromDecimal2.toString();							// Converts to string to ready for output
		return frontZero2 + finalBinary;											// Outputs 32-bit binary string with zero padding
	}
	
	/**
	 * This is method breaks down the binary instruction string, then through
	 * a series of if-statements filters them by the segments produced to
	 * find which operation is required. It then passes the segments to the 
	 * static methods in the <code>Operations</code> class.
	 * 
	 * If, after breaking it down none of the available operations were executed
	 * (as in an invalid <code>opcode</code> or <code>funct</code> was input),
	 * it outputs: "Illegal Instruction!" and exits.
	 * 
	 * @param binary The 32 bit binary string that will be broken down.
	 */
	public static void breakDown(String binary)				// Breaks down the binary string into the format parts
	{
		String opcode = binary.substring(0, 6);
		String rs = binary.substring(6, 11);
		String rt = binary.substring(11, 16);
		String rd = binary.substring(16, 21);
		String shamt = binary.substring(21, 26);
		String funct = binary.substring(26, 32);
		String immediate = binary.substring(16, 32);

		if(opcode.equals("000000"))							// This series of if-statements checks the opcode and funct to find operation
		{
			if(funct.equals("100000"))						// ADD
			{
				ADD(rs, rt, rd);
			}
			else if(funct.equals("100100"))					// AND
			{
				AND(rs, rt, rd);
			}
			else if(funct.equals("100111"))					// NOR
			{
				NOR(rs, rt, rd);
			}
			else if(funct.equals("101010"))					// SLT
			{
				SLT(rs, rt, rd);
			}
			else if(funct.equals("101011"))					// SLTU
			{
				SLT(rs, rt, rd); 				// My reasoning is that since we're using integers, Java doesn't care so SLT is fine with signed or unsigned. BE PREPARED TO FIX
			}
			else if(funct.equals("000000"))					// SLL
			{
				SLL(rt, rd, shamt);
			}
			else if(funct.equals("000010"))					// SRL
			{
				SRL(rt, rd, shamt);
			}
			else
			{
				System.out.println("Illegal Instruction!");
				System.exit(0);
			}
		}
		else if(opcode.equals("001000"))					// ADDI
		{
			ADDI(rs, rt, immediate);
		}
		else if(opcode.equals("000100"))
		{
			BEQ(rs, rt, immediate);				// BEQ
		}
		else if(opcode.equals("000101"))
		{
			BNE(rs, rt, immediate);				// BNE
		}
		else
		{
			System.out.println("Illegal Instruction!");
			System.exit(0);
		}
	}
	
	/**
	 * This <code>jump</code> variable is used for the BNE and BEQ operations.
	 * When one of the operations for those equations is called for, the value for
	 * <code>jump</code> is set to true and in the <code>UI</code>, if it is true 
	 * then it undergoes the procedure to jump. After the jump, it is set back to false.
	 */
	public static boolean jump = false;
	
	/**
	 * This <code>jumpAddress</code> variable is used in this class to determine
	 * where in the address to jump to. It is then taken by the <code>UI</code> 
	 * class and sets the reading to the address specified.
	 */
	public static int jumpAddress;
	
	/**
	 * This method is used as a tool throughout other methods in this class.
	 * It is usually meant to take the binary register address and convert it
	 * to decimal. This decimal value is needed for it to be usable in the 
	 * <code>Register</code> class's <code>registers[]</code> where it can 
	 * retrieve the specified register.
	 * 
	 * @param register The register address in binary
	 * @return The register address converted to decimal
	 */
	public static int toDecimal(String register)
	{
		int decimalValue = Integer.parseInt(register, 2);
		return decimalValue;
	}
	
	/*
	 * Although many of the methods do different functions, the methods from
	 * ADD to SRL function on the same principle as they all require MIPS'
	 * R-Format. They find the register addresses, take the value at the addresses,
	 * do the specified operation on them, and place the value into the rd register.
	 */
	
	/** 
	 * This ADDs the values at <code>rs</code> and <code>rt</code> to <code>rd</code>.
	 *
	 * (<code>rd</code> = <code>rs</code> + <code>rt</code>)
	 * 
	 * @param rs Address of <code>rs</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void ADD(String rs, String rt, String rd)
	{
		int rsDecimal = toDecimal(rs);									// These take all the register inputs and converts them to decimal integers
		int rtDecimal = toDecimal(rt);									// This step is needed because we need to summon these particular registers
		int rdDecimal = toDecimal(rd);									// found in the Registers class
		
		int rtValue = registers[rtDecimal];					// These get the value at each required register
		int rsValue = registers[rsDecimal];
		
		int rdValue = rtValue + rsValue;								// Adds the register contents into rdValue
		
		setRegister(rdDecimal, rdValue); 															// This takes changes rd's register to become rdValue
	}
	
	/**
	 * This ANDs <code>rs</code> and <code>rt</code>, places the result into <code>rd</code>.
	 * (<code>rd</code> = <code>rs</code> & <code>rt</code>)
	 * 
	 * @param rs Address of <code>rs</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void AND(String rs, String rt, String rd)
	{
		int rsDecimal = toDecimal(rs);
		int rtDecimal = toDecimal(rt);
		int rdDecimal = toDecimal(rd);
		
		int rtValue = registers[rtDecimal];
		int rsValue = registers[rsDecimal];
		
		int rdValue = rtValue & rsValue;								// Sets bitwise operator & between the two values
		
		setRegister(rdDecimal, rdValue);						// Sets the destination register with the value
	}
	
	/**
	 * This NORs <code>rs</code> and <code>rt</code>, places the result into <code>rd</code>.
	 * (<code>rd</code> = !(<code>rs</code> | <code>rt</code>))
	 * 
	 * @param rs Address of <code>rs</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void NOR(String rs, String rt, String rd)
	{
		int rsDecimal = toDecimal(rs);
		int rtDecimal = toDecimal(rt);
		int rdDecimal = toDecimal(rd);
		
		int rtValue = registers[rtDecimal];
		int rsValue = registers[rsDecimal];
		
		int rdValue = ~(rtValue | rsValue);								// Sets bitwise operators NOT and OR
		
		setRegister(rdDecimal, rdValue);
	}
	
	/**
	 * If <code>rs</code> < <code>rt</code>, <code>rd</code> = 1. Otherwise, <code>rd</code> = 0.
	 * 
	 * @param rs Address of <code>rs</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void SLT(String rs, String rt, String rd)
	{
		int rsDecimal = toDecimal(rs);
		int rtDecimal = toDecimal(rt);
		int rdDecimal = toDecimal(rd);
		
		int rtValue = registers[rtDecimal];
		int rsValue = registers[rsDecimal];
		
		if(rsValue < rtValue)											// If rs < rt, rd = 1
		{
			setRegister(rdDecimal, 1);
		}
		else															// If rs >= rt, rd = 0
		{
			setRegister(rdDecimal, 0);
		}
	}
	
	/**
	 * This shifts <code>rt</code> left <code>shamt</code> times, and stores the result in <code>rd</code>.
	 * 
	 * @param shamt Address of <code>shamt</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void SLL(String rt, String rd, String shamt)
	{
		// reg rd = reg rt << shamt
		int rtDecimal = toDecimal(rt);
		int rdDecimal = toDecimal(rd);
		int shamtDecimal = toDecimal(shamt);
		
		int rtValue = registers[rtDecimal];
		
		int rdValue = rtValue << shamtDecimal;							// rd = rt shifted left by rs
		
		setRegister(rdDecimal, rdValue);
	}
	
	/**
	 * This shifts <code>rt</code> right <code>shamt</code> times, and stores the result in <code>rd</code>.
	 * 
	 * @param rs Address of <code>rs</code> register
	 * @param rt Address of <code>rt</code> register
	 * @param rd Address of <code>rd</code> register
	 */
	public static void SRL(String rt, String rd, String shamt)
	{
		int rtDecimal = toDecimal(rt);
		int rdDecimal = toDecimal(rd);
		int shamtDecimal = toDecimal(shamt);
		
		int rtValue = registers[rtDecimal];
		
		int rdValue = rtValue >> shamtDecimal;							// rd = rt shifted right by rs
		
		setRegister(rdDecimal, rdValue);
	}
	
	/*
	 * The methods below deal with MIPS' J-Format where an immediate
	 * or address is involved.
	 */
	
	/** 
	 * This adds a constant to <code>rs</code> and stores the value in <code>rt</code>.
	 * It also checks if <code>immediate</code> is negative or not, and adjusts 
	 * appropriately.
	 * 
	 * @param rs The location of the <code>rs</code> registry
	 * @param rt The location of the <code>rt</code> registry
	 * @param immediate The value of the constant
	 */
	public static void ADDI(String rs, String rt, String immediate)
	{
		if(immediate.startsWith("1"))									// immediate is signed, so starting with a 1 means it's negative
		{
			int rsDecimal = toDecimal(rs);
			int rtDecimal = toDecimal(rt);
			int immDecimal = (toDecimal(immediate) - (2 * (2^16)));		// Takes away the positive 16th bit and makes a negative 16th bit
			
			int rsValue = registers[rsDecimal];
			
			int rtValue = rsValue + immDecimal;					
			
			setRegister(rtDecimal, rtValue);
		}
		else															// Other wise continues normally
		{
			int rsDecimal = toDecimal(rs);
			int rtDecimal = toDecimal(rt);
			int immDecimal = toDecimal(immediate);
			
			int rsValue = registers[rsDecimal];
			
			int rtValue = rsValue + immDecimal;							// rt = rs + immediate
			
			setRegister(rtDecimal, rtValue);
		}
	}
	
	/**
	 * If <code>rs</code> =/= <code>rt</code>, go to <code>address</code>
	 * 
	 * @param rs The location of the <code>rs</code> registry
	 * @param rt The location of the <code>rt</code> registry
	 * @param address The address to go to.
	 */
	public static void BNE(String rs, String rt, String address)
	{
		int rsDecimal = toDecimal(rs);
		int rtDecimal = toDecimal(rt);
		int addDecimal = toDecimal(address);
		
		int rtValue = registers[rtDecimal];
		int rsValue = registers[rsDecimal];
		
		if(rtValue != rsValue)
		{
			if(address.startsWith("1"))									// That means it is signed and therefore negative
			{
				addDecimal = addDecimal - 2 * (32768);					// Subtracting 2^(15) makes it signed since it is 16 bits
			}
			jump = true;
			jumpAddress = addDecimal;
		}
	}
	
	/**
	 * If <code>rs</code> == <code>rt</code>, go to <code>address</code>
	 * 
	 * @param rs The location of the <code>rs</code> registry
	 * @param rt The location of the <code>rt</code> registry
	 * @param address The address to go to.
	 */
	public static void BEQ(String rs, String rt, String address)
	{
		int rsDecimal = toDecimal(rs);
		int rtDecimal = toDecimal(rt);
		int addDecimal = toDecimal(address);
		
		int rtValue = registers[rtDecimal];
		int rsValue = registers[rsDecimal];
		
		if(rtValue == rsValue)
		{
			if(address.startsWith("1"))
			{
				addDecimal = addDecimal - 2 * (32768);
			}
			jump = true;
			jumpAddress = (addDecimal - originalAddress)/4;
		}
	}
	
	/**
	 * This variable is used once every time the program runs. This works to abstract the
	 * data for the address and instructions by setting the first address to zero. All
	 * subsequent addresses will be compared to the original address and divided by 4 to
	 * increase in increments of 1. This is done to make the BEQ and BNE operations easier,
	 * as well as to allow the address/instruction pair to fit into the <code>input[]</code> 
	 * array.
	 */
	public static int originalAddress;
	
	/**
	 * The current address is used after setting the original address. This works to help enter
	 * each instruction into the <code>input[]</code> array in order of top to bottom based on 
	 * the input.
	 */
	public static int currentAddress;
	
	/**
	 * For the <code>input[X]</code> array, X represents the address if for the first address,
	 * X = 0. <code>input[X]</code> holds the instruction of the Xth address. This system for 
	 * the array is what organizes the address/instruction information from the input to allow 
	 * for jumps. The reason that <code>input[]</code> holds 100 values is that I didn't want 
	 * to make an array that dynamically changes based on the amount of instructions. Seeing 
	 * that the <code>input[]</code> array is not visible by the user and the code is 
	 * "lightweight", I just set it to an arbitrary large number of 100. That should be more 
	 * than enough anyways.
	 */
	
	static int input[] = new int[100];
	
	/**
	 *The <code>registers[]</code> array is used to hold MIPS' 32 data registers.
	 */
	static int registers[] = new int[32];
	
	/**
	 * This must be initialized by the UI first to set every value of the <code>registers[]</code> 
	 * array to zero.
	 */
	public static void createRegister()
	{
		for(int i = 0; i < 32; i++)
		{
			registers[i] = 0;
		}
	}
	
	/**
	 * This must be initialized by the UI first to set every value of the <code>input[]</code>
	 * to zero.
	 */
	public static void createInput()
	{
		for(int i = 0; i < 100; i++)
		{
			input[i] = 0;
		}
	}
	
	/**
	 * This method allows modification of the values in the <code>register[]</code> array
	 * @param registerNumber The location of the register, must be between 0 and 31.
	 * @param registerValue	The value to put into the register.
	 */
	public static void setRegister(int registerNumber, int registerValue)
	{
		registers[registerNumber] = registerValue;
	}
	
	/**
	 * This method allows modification of the values in the <code>input[]</code> array to add 
	 * instructions in decimal format to the address.
	 * @param address The number of the register, must be between 0 and 99 and of type
	 * 					<code>int</code>.
	 * @param instruction The decimal version of the instruction, must be of type <code>int</code>.
	 */
	public static void setInput(int address, int instruction)
	{
		input[address] = instruction;
	}
}