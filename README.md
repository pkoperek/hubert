hubert
======

Open Source implementation of algoritms described in:
[Distilling Free-Form Natural Laws from Experimental Data]

It is a part of research carried out on [AGH University of Science and Technology, Faculty of Computer Science, Electronics and Telecommunications, Department of Computer Science]

[![Build Status](https://travis-ci.org/pkoperek/hubert.svg?branch=master)](https://travis-ci.org/pkoperek/hubert)

Developing
==========

 * `cd src/main/webapp && bower install`
 * `cd ../../../`
 * `$ sbt`
 *  ` $ > ~container:start` - `~` turns on the autoreload
 * Open browser at `http://localhost:8080`

Building
========

 * `sbt clean package`

 * Result can be found in `target/`

Samples
=======

Sample datasets can be found in `samples/` directory.

#### x2_add_1.csv

Original formula: `x = t^2 + 1`
The accuracy (target error) is 0.01 (this is the resolution of datapoints).

Sample output from the evolution:
```
17:55:10.823 [pool-1-thread-6] DEBUG p.e.a.h.e.DeterministicCrowdingEvolutionIteration => Evolution iteration: end max (best): -> EvaluatedIndividual(Individual: ((var_0(dep of: ) * var_0(dep of: )) - var_1(dep of: )),-3.197789255612609E-14)
```

Which roughly (constants omitted) translates to the following equation: `t * t - x = 0`

#### circle.csv

Original formula: `16 = x^2 + y^2`

Sample output from the evolution:
```
18:09:22.132 [pool-1-thread-6] DEBUG p.e.a.h.e.DeterministicCrowdingEvolutionIteration => Evolution iteration: end max (best): -> EvaluatedIndividual(Individual: (((0.12389272997871958 - var_0(dep of: )) + (var_0(dep of: ) + (var_1(dep of: ) + ((0.237852231098711 - var_1(dep of: )) - (var_0(dep of: ) * var_0(dep of: )))))) + (0.715395558549027 + (0.9672931591150952 - (0.6886642219802059 - (var_1(dep of: ) * var_1(dep of: )))))),-1.2429467177658014E-13)

```

Which translates to the following equation: `-x*x  + y*y + 1.355769457 = 0`

### Limitations:

* Fitness function uses the absolute value of the partial derivatives quotient. This means that the signs in the equations can be incorrect.

References
==========

[Distilling Free-Form Natural Laws from Experimental Data]: http://www.sciencemag.org/content/324/5923/81.abstract
[AGH University of Science and Technology, Faculty of Computer Science, Electronics and Telecommunications, Department of Computer Science]: http://www.ki.agh.edu.pl/en

License
=======

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

Disclaimer
==========

I am providing code in this repository to you under an open source license. Because this is my personal repository the license to my code is from me and not from my employer (Facebook).
