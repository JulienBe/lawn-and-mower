# ants analogy

leave traces behind

traces have:
 - owner
 - pos
 - turn number

if there is already a trace:
 - send a message to the owner
 - invalidate your move

on message receive:
 - rollback to the move before this trace

rollback options:
 1. just save the world on each turn. Sounds like a big choking point, not very //
 2. roll me back to this save. Let others continue their merry lives
 
 blablabla.. Meh. Implies too much communication
 
 # Or...
 
 find a way to filter out invalid commands.. a way that doesn't imply actually running the simulation ?
 
# Don't bother

1. Run the simulation for everyone, don't bother with overlaps. Store path for everyone in its own memory to avoid locks. 
2. find earliest overlap
3. remove the invalid command 
4. go back to 1

Don't remove oob commands from the list, they might become valid at some point because of an earlier overlap

## Find overlap 

In the end, it will be a list of coordinates associated to time.

As coordinates can easily be flatten, it would look like `List<Pair<Int, Int>>` per mower

Could also be a matrix of dimensions highest number of turns and number of mowers

But that matrix would anyway have to be constructed from all those lists.. Or maybe they use it as a shared memory ?

I could also consider each move as a line, and the result as a geometric form, but meh... 