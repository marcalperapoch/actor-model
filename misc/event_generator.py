from random import randint,shuffle
import math

TOTAL_IDS = 1000
MAX_IMPRESSIONS = 300
PROB_CLICK = 40
PROB_BUY = 10

NUM_OUTPUT_FILES = 3

result_file = "expected_result.csv"

events = []
total_events = 0

with open(result_file, 'w') as out:
    out.write("id,impression,click,buy\n")

    for id in range(1,TOTAL_IDS + 1):
        impressions = randint(0, MAX_IMPRESSIONS)
        clicks = 0
        buys = 0
        for j in range(0, impressions):
            events.append("impression,{}".format(id))
            clicked = randint(0, 100)
            if clicked <= PROB_CLICK:
                clicks += 1
                events.append("click,{}".format(id))
                if clicked <= PROB_BUY:
                    buys += 1
                    events.append("buy,{}".format(id))

        if impressions > 0:
            out.write("{},{},{},{}\n".format(id, impressions, clicks, buys));
            total_events += impressions + clicks + buys

shuffle(events)

print("Generated {} events. Writing them in {} files...".format(total_events, NUM_OUTPUT_FILES))

file_index = 1
events_per_page = math.ceil(total_events/NUM_OUTPUT_FILES)
for i in range(0, total_events, events_per_page):

    fileName = "../examples/src/main/resources/events_{}.csv".format(file_index)

    with open(fileName, 'w') as out:

        to = min(i+events_per_page, total_events)
        for event in events[i:to]:
            out.write("{}\n".format(event))

    file_index += 1