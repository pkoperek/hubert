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

List:

 * `x_2_add_1.csv` - contains data for `x^2+1` function. The accuracy (target error) is 0.01 (this is the resolution of datapoints).

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
