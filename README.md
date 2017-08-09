# Uncommons Maths
[![Build Status](https://travis-ci.org/Pr0methean/uncommons-maths.svg?branch=master)](https://travis-ci.org/Pr0methean/uncommons-maths)

| Class                   | Seed size (bytes)  | Period (bits)      | Performance | Effect of setSeed(long)     |
|-------------------------|--------------------|--------------------|-------------|-----------------------------|
| AESCounterRNG           | 16, 24, 32, 40, 48 | 2<sup>135</sup>    | Medium      | Combines with existing seed |
| CellularAutomatonRNG    |                  4 | ?                  | Medium      | Replaces existing seed      |
| CMWC4096RNG             |              16384 | 2<sup>131104</sup> | Fast        | No-op                       |
| MersenneTwisterRNG      |                 16 | 2<sup>19937</sup>  | Fast        | No-op                       |
| XORShiftRNG             |                 20 | ~2<sup>160</sup>   | Fastest     | No-op                       |

See https://maths.uncommons.org