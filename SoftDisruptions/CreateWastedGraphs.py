import matplotlib.pyplot as plt
import numpy as np


iso_wasted_blocks = {}
soft_wasted_blocks = {}
with open('soft_disruption_wasted_blocks.txt', "r") as f:
	for line in f.readlines():
		part = float(line.split(":")[0].split("_")[1])
		data = [float(i) for i in line.split(":")[-1].split(",")]
		if line.startswith("iso"):
			iso_wasted_blocks[part] = data
		else:
			soft_wasted_blocks[part] = data
			
print(data, [x[1] for x in sorted(iso_wasted_blocks.items())])

fig, ax1 = plt.subplots()

ax1.errorbar([x for x in sorted(iso_wasted_blocks.keys())], 
			 [np.mean(x) for _, x in sorted(iso_wasted_blocks.items())],
			 [np.std(x) for _, x in sorted(iso_wasted_blocks.items())],
			 label="Isolation Average")
			 
ax1.plot([x for x in sorted(iso_wasted_blocks.keys())], 
			 [max(x) for _, x in sorted(iso_wasted_blocks.items())],
			 'b--', label="Isolation Max")
			 
ax1.errorbar([x for x in sorted(soft_wasted_blocks.keys())], 
			 [np.mean(x) for _, x in sorted(soft_wasted_blocks.items())],
			 [np.std(x) for _, x in sorted(soft_wasted_blocks.items())],
			 label="Soft Average")
			 
ax1.plot([x for x in sorted(soft_wasted_blocks.keys())], 
			 [max(x) for _, x in sorted(soft_wasted_blocks.items())],
			 'r--', label="Soft Max")
			 
ax1.set_xlabel("Proportion of Total Bandwidth Remaining")
ax1.set_ylabel("Number of Competing Blocks")
plt.title("Competing Blocks for Soft Disruptions")
plt.xscale('log')
lines, labels = ax1.get_legend_handles_labels()
#lines2, labels2 = ax2.get_legend_handles_labels()
#ax2.legend(lines + lines2, labels + labels2, loc=0)
ax1.legend(loc=1)

plt.savefig("wasted_blocks_soft.png")
