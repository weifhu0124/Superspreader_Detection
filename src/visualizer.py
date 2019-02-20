import numpy as np
import os
from matplotlib import pyplot as plt

# load spreader data
def load_data(path, spreaders):
	error = 0
	with open(path, 'r') as fp:
		for line in fp:
			entry = line.split(',')
			if len(entry) < 2:
				error += 1
				continue
			if entry[0] not in spreaders:
				spreaders[entry[0]] = 0
			spreaders[entry[0]] += int(entry[1])
	print(error)
	return spreaders

# get the counts based on k
def get_count(spreaders):
	ks = np.arange(25, 50, 1)
	counts = []
	histo = []
	for k in ks:
		count = 0
		for spreader in spreaders:
			if spreaders[spreader] >= k:
				count += 1
		counts.append(count)
	for spreader in spreaders:
		if spreaders[spreader] > 50:
			histo.append(spreaders[spreader])
	return ks, counts, histo

# plot the data
def plotter(x, y, histo):
	plt.title('Number of Superspreaders vs the threshold k')
	plt.plot(x, y, '-o')
	plt.xlabel('The threshold values for k')
	plt.ylabel('Number of Superspreaders')
	plt.show()
	plt.hist(histo, bins=50)
	plt.title('Histogram for number of distinct host connections')
	plt.xlabel('Number of distinct host connections')
	plt.ylabel('Frequencies')
	plt.show()

if __name__ == '__main__':
	spreaders = {}
	spreaders = load_data('result/spreaders.txt', spreaders)
	ks, counts, histo = get_count(spreaders)
	plotter(ks, counts, histo)
	print('done')