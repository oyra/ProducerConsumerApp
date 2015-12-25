# ProducerConsumerApp
There are Producer and Consumer.
Producer imitates the process of collecting data in the background. 

Consumer represents sending data to backend servers, in our case it updates the user view rather than send the data to real backend
Producer and Consumer use queue to exchange messages (it’s a priority queue, please see below).

- Producer Job:
Every T1 seconds it generates tuples of type (string, random weight)
Where string is arbitrary string of size 4 and random weight is a random number from 1 to 10. After tuple is generated producer puts the tuple into priority queue.
Queue supports prioritization, e.g. tuple with weight 10 should be taken earlier than a tuple with weight 1.

- Consumer Job:
Every T2 seconds it checks a queue.
If there is a tuple in the queue Consumer takes it and updates the user view with the most important (with the largest weight) data and queue size.

Consumer might fail with 20% probability after the tuple has been taken from the queue. In this case the data needs to be returned to the queue.

***
NB In a real situation we’ll need to clear the queue from time to time in some way (removing the low-priority data, dumping data to db) otherwise it may grow and grow (the velocity of growing depends on the services’ timeouts and the errors' frequency)
