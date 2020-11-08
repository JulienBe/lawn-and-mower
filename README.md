# Compile it

Using `mvn`, `./mvnw` or even `./mvnw.cmd` depending on your platform.

You need at least Java 15 to compile or run it

`mvn clean package` will run the tests and produce:
 - a small jar at `target/lawn-and-mower-1.0.0-runner.jar.original` (< 30k) 
 - a fat jar at `target/lawn-and-mower-1.0.0-runner.jar` (< 3m) 

`mvn clean package -Dquarkus.container-image.build=true` will produce a docker image named `lawn-and-mower` with tag `1.0.0`

`mvn compile quarkus:dev` will compile into dev mode for fast rerun when you modify something

`mvn compile quarkus:dev -Dquarkus.args="path_to_file` will process your file.  
Just press `enter` in the terminal to rerun it

You can also process multiple files, enter a multiline string. See `mow.sh`

Tips: you can generate random data with the regex at https://onlinerandomtools.com/generate-random-data-from-regexp

# Run it

### Jar

Other than the 'compile' options, you can use the jars (fat and slim) located into the deliverables folder.

As previously stated, you will need at least java 15

You can either run it with:
- `no arguments` : `java --enable-preview -jar deliverables/chosen.jar`. It will then run the given example and any file located at `/opt/lawn_and_mower/inputs/`
- `file as argument` : add for example `"test_inputs/huge_lawn"` after the previous run command. Will **not** run files located at the default input folder

### rest api

The very cleverly named branch put-api-to-rest contains a version with a basic rest api.

It deploys to port `8080` on `/mow`

You add a json representing the test you want to run, and it gets added to the pool.

When 

### Docker

You can run the docker image and mount a volume pointing to your files: `docker run -v path_inputs:/opt/lawn_and_mower/inputs/ julienbe/lawn-and-mower:1.0.0`

The 1.1.0 image is the one running with the rest api and not the cli. It deploys internally on port `8080`. Run it with `docker run -p 25852:8080 julienbe/lawn-and-mower:1.1.0`

- Add an input to the list: `curl -X POST -H 'Content-Type: application/json' -i http://localhost:25852/mow --data '{"lawn":"5 5","mowers":[{"coord":"1 2 N","instructions":"LFLFLFLFF"},{"coord":"3 3 E","instructions":"FFRFFRFRRF"}]}'`
- Get the result with a simple `curl http://localhost:25852/mow`


### Output

In any case, the output will be in the logs
If you run it in 'folder mode' (no arguments) it will also try to write the corresponding output to a file in `/opt/lawn_and_mower/ouputs`
For the docker version, you can also mount it as a volume with `docker run -v path_inputs:/opt/lawn_and_mower/inputs/ output_folder:/opt/lawn_and_mower/inputs/ julienbe/lawn-and-mower:1.0.0`

*It's using user id 1001*

### Example

- build : https://asciinema.org/a/1eSrZO4a8Oo4qjIabejVc7S9M
- docker : https://asciinema.org/a/v68vTeqRZAsDyGdiaa2cWjVKJ
- rest : https://asciinema.org/a/9p396VPRRJzOIxPjocHJbSnS5
- jar : https://asciinema.org/a/4fSvq8oOU4CMJM1gC7Ie6kcoU
- moo.sh : https://asciinema.org/a/WHB3u482XA7s50Pj6EMYxoHAh

# Goal

Build a program that implements the following mower specification.

## Pitch

The client, company X, wants to develop an algorithm to mow **rectangular surfaces**. 
`A bit confusing as movements are actually already in the inputs`

Mowers can be programmed to move throughout the **entire surface**.

## Specs as given
 
A mower's position is represented by **Cartesian coordinates (X, Y)**, and an **orientation (N, E, W, S)**.

The lawn is divided into a **grid** to simplify navigation.

*For example, the position 0,0,N indicates the mower is in the lower left corner of the lawn facing north.*

To move the mower, we use a series of a combination of the following **commands: L, R, and F**.

**L** and **R** **turn** the mower 90° left or right **without moving** the mower.
**F** means **move forward** one space in the direction the mower is currently facing without changing its orientation.

If a forward movement would cause the mower to **move outside** of the lawn, the mower **remains** in the position **and** this **command is discarded**.
 
The position directly to the north of *(X, Y)* is *(X, Y+ 1)* and the position to the east of *(X, Y)* is *(X + 1, Y)*.

Different mowers may **not occupy the same space at the same time**,
 and if a mower receives a **move forward** instruction that would cause it to **run into another mower**,
 the move forward instruction is **silently discarded**.
 
Your simulation will be run on a machine with multiple CPUs so multiple **mowers should be processed simultaneously** in order to speed up the overall execution time.

## Inputs

The mowers are programmed using an input **file** constructed in the following manner:

The **first line** corresponds to the **upper right corner of the lawn**.
The **bottom left** corner is implicitly **(0, 0)**.

The rest of the file describes the **multiple mowers** that are on the lawn.
Each **mower** is described on **two lines**:

The **first** line contains the mower's **starting position and orientation** in the format "X Y O". `with a space`
X and Y are the coordinates and O is the orientation.

The **second** line contains the instructions for the mower to **navigate the lawn**.
The instructions are **not separated by spaces**.

## Output

At the end of the simulation, the **final position and orientation of each mower is output in the order that the mower appeared in the input**.

When designing and implementing your solution ensure that you keep in mind separation of concerns, performance, and testing.

## Example

Input file

```
5 5
1 2 N
LFLFLFLFF
3 3 E
FFRFFRFRRF
```

Result

```
1 3 N
5 1 E
```

# Assumptions 

## Should be discussed

- Mowers have a fixed size of 1 ?
- If two mowers plan to move on the same square next turn, both moves would be invalidated. A bit like platinium rift
- They only care about the final position, not if they actually did their job ? Meh.
- looks like we favor cpu consumption over memory consumption

## Should be checked

- The grid has a size of at least one.

# Todos

- add picocli
- add option to write result at the end of file
- ~~add~~ useful ~~rest endpoint~~ 
- check on non linux machines
- deploy
- check not logged to dockerhub
- ~~asciinema~~
- ~~deliverables~~
- ~~bench~~
- ~~damn regex doing a stackoverflow~~
- add proper test for session creator