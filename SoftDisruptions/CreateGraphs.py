import matplotlib.pyplot as plt


iso_prop = {}
iso_fork = {}
link_prop = {}
link_fork = {}
with open('soft_disruption_propagation_time.txt', "r") as f:
	for line in f.readlines():
		t = line.split(":")[0]
		data = [float(i) for i in line.split(":")[-1].split(",")]
		val = float(t.split("_")[-1])
		if t.startswith("iso"):
			iso_prop[val] = data[:3]
			iso_fork[val] = data[3:]
		else:
			link_prop[val] = data[:3]
			link_fork[val] = data[3:]
			
print(iso_prop, link_fork, [x[1] for x in sorted(iso_prop.items())])

fig, ax1 = plt.subplots()

ax2 = ax1.twinx()

ax1.errorbar([x for x in sorted(iso_prop.keys())], 
			 [x[0] for _, x in sorted(iso_prop.items())],
			 [x[1] for _, x in sorted(iso_prop.items())],
			 label="Isolation Cut Avg")
			 
ax1.errorbar([x for x in sorted(link_prop.keys())], 
			 [x[0] for _, x in sorted(link_prop.items())],
			 [x[1] for _, x in sorted(link_prop.items())],
			 label="Link Cut Avg")
			 
ax2.plot([x for x in sorted(iso_prop.keys())], 
			 [x[2] for _, x in sorted(iso_prop.items())],
			 'b--', label="Isolation Cut Worst")
			 
ax2.plot([x for x in sorted(link_prop.keys())], 
			 [x[2] for _, x in sorted(link_prop.items())],
			 'r--', label="Link Cut Worst")
			 
ax1.set_xlabel("Proportion of Total Bandwidth Remaining")
ax1.set_ylabel("Average Propagation Time (ms)")
ax2.set_ylabel("Worse Case Propagation Time (ms)")
plt.title("Propagation Time for Soft Disruptions")
plt.xscale('log')
ax2.set_yscale('log')
lines, labels = ax1.get_legend_handles_labels()
lines2, labels2 = ax2.get_legend_handles_labels()
ax2.legend(lines + lines2, labels + labels2, loc=0)

plt.savefig("avg_prop_time_soft.png")
