# Dual-Mode MIPS Instruction Set Architecture (ISA) Simulator

A cycle-accurate functional simulator for the **32-bit MIPS Instruction Set Architecture**, developed entirely in **Java**. This simulator is capable of parsing, decoding, and executing both human-readable **MIPS Assembly (`.asm` / `.s`)** programs and raw **32-bit Binary/Hex Machine Code** directly.

## Architectural Highlights & Features

* **Dual Execution Pipeline (Assembly & Machine Code):**
  * **Assembly Mode:** Features a custom lexical parser and assembler engine that resolves symbolic labels, translates mnemonics, and maps register identifiers into executable instructions.
  * **Machine Code Mode:** Includes a bit-level instruction decoder that extracts bitfields (`opcode`, `rs`, `rt`, `rd`, `shamt`, `funct`, and 16-bit/26-bit `immediate/address` offsets) across **R-Type, I-Type, and J-Type** instruction formats.
* **Simulated Processor State:** Full emulation of the 32 general-purpose MIPS registers (`$zero` to `$ra`), Program Counter (`PC`) control flow (branches, jumps, and sequential increments), Arithmetic Logic Unit (ALU) operations, and segmented Data/Instruction Memory space.
* **Interactive CLI Loader:** Built-in command-line interface to dynamically switch between execution modes and load benchmark programs directly from the input test suite.

## Project Structure

* `src/`: Contains the core Java implementation of the MIPS datapath, instruction decoder, assembler parser, register file, and memory units.
* `src/Inputs/`: Directory housing sample MIPS assembly scripts and machine code test programs for execution and verification.

## Getting Started & Usage

1. Clone the repository and open the project in any Java IDE (e.g., IntelliJ IDEA) or compile the source files inside `src/`.
2. Run the `Main` class to launch the interactive simulator prompt.
3. **Select Execution Mode:** Choose between **Assembly Code** or **Machine Code** execution at the prompt.
4. **Select Input Program:** Choose the target benchmark/program file from the `src/Inputs` directory to execute and inspect the resulting register and memory states.

## Tech Stack
* **Language:** Java (100%)
* **Architecture:** 32-bit MIPS ISA
