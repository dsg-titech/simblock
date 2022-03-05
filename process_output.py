import json
import numpy as np
import matplotlib.pyplot as plt
from sklearn.metrics import auc
from collections import defaultdict, deque


def get_average_propogation_time(apt, id, time, output):
	times = []
	for event in output:
		if event["kind"] == "add-block":
			if event['content']['block-id'] == id and int(event['content']['timestamp']) > time:
				times.append(int(event['content']['timestamp']) - time)
				
	if len(times) != 0:
		apt[time] = [np.mean(times), np.std(times)]
		
		
def get_block_at_time(time, adds):
	for i, (id, timestamp) in enumerate(adds):
		if timestamp > time:
			return adds[i-1][0]
			
def analyze_agreement(pa):
	drops = []
	magnitudes = []
	running_set = deque(40*[1], 40)
	running_means = []
	slopes = []
	lines = []
	last_time = float("-inf")
	coords = None
	max_val = float("-inf")
	max_time = None
	for i, (val, time) in enumerate(pa):
		if val > max_val:
			max_val = val
			max_time = time
		running_set.appendleft(val)
		running_means.append(np.mean(running_set))
		if val < running_means[-1] - 0.1 and time-last_time > 100000:
			last_time = time
			window = [v for v, _ in pa[i-30:i+30]]
			drops.append((time, val, max(window), min(window)))
			magnitudes.append(max(window) - min(window))
			if coords is not None:
				lines.append([max_val, drops[-2][3], max_time, drops[-2][0]])
				slopes.append((max_val-drops[-2][3])/(max_time-drops[-2][0]))
			max_val = float("-inf")
			coords = 1
	
	#slopes = []
	#for i in range(1, len(drops)):
	#	print(drops[i][2], drops[i-1][3], drops[i][0], drops[i-1][0])
	#	slopes.append((drops[i][2]-drops[i-1][3])/(drops[i][0]-drops[i-1][0]))
	return drops, running_means, magnitudes, slopes, lines


with open(".\\simulator\\src\\dist\\output\\output.json") as f:
	output = json.load(f)
	
average_propagation_time = {}
seen_block_ids = set()
node_vals = defaultdict(list)
max_timestamp = 0
for event in output:
	if event["kind"] == "add-block":
		id = event['content']['block-id']
		timestamp = int(event['content']['timestamp'])
		max_timestamp = max(timestamp, max_timestamp)
		node_vals[event['content']['node-id']].append([id, timestamp])
		if id not in seen_block_ids:
			get_average_propogation_time(average_propagation_time, id, timestamp, output)
		seen_block_ids.add(id)

percentage_agreement = []
for time in range(30000, max_timestamp, 100):
	sums = defaultdict(int)
	for k, v in node_vals.items():
		b = get_block_at_time(time, v)
		sums[b] += 1
	if max(sums, key=sums.get) is not None:
		percentage_agreement.append([sums[max(sums, key=sums.get)]/len(node_vals), time])
	
events, means, drop_diffs, increase_rates, increase_vis = analyze_agreement(percentage_agreement)
print(drop_diffs, increase_rates, auc([x[1] for x in percentage_agreement], [x[0] for x in percentage_agreement]))
plt.plot([x[1] for x in percentage_agreement], [x[0] for x in percentage_agreement])
plt.fill_between([x[1] for x in percentage_agreement], 0, [x[0] for x in percentage_agreement], alpha=0.2)
plt.plot([x[0] for x in events], [x[1] for x in events], 'ro')
for line in increase_vis:
	plt.plot([line[2], line[3]], [line[0], line[1]], 'g')
plt.ylabel("Precentage of Nodes with Majority Block")
plt.xlabel("Timestamp")
plt.title("Percentage Agreement")
plt.show()
plt.clf()
	
plt.errorbar([x for x in sorted(average_propagation_time.keys())], 
			 [x[0] for _, x in sorted(average_propagation_time.items())],
			 [x[1] for _, x in sorted(average_propagation_time.items())])
plt.xlabel("Time that block is mined")
plt.ylabel("Average Time Between Block Being Mined and Recieved")
plt.title("Average Propagation Time")
plt.show()
