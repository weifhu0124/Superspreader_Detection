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
	ks = np.arange(25, 35, 1)
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

# get the counts of data with respect to time
def get_count_by_time(k=200):
	times = np.arange(1, 7, 1)
	counts = []
	for t in times:
		spreaders = load_data('tmp/spreaders-' + str(t-1) +'.txt', {})
		count = 0
		for spread in spreaders:
			if spreaders[spread] >= k:
				count += 1
		counts.append(count)
	return times, counts

# plot the data
def plotter(x, y, xlable, title, histo=None):
	plt.title(title)
	plt.plot(x, y, '-o')
	plt.xlabel(xlable)
	plt.ylabel('Number of Superspreaders')
	plt.show()
	if histo != None:
		plt.hist(histo, bins=50)
		plt.title('Histogram for number of distinct host connections')
		plt.xlabel('Number of distinct host connections')
		plt.ylabel('Frequencies')
		plt.show()


# combine files
def combine_files():
	filenames = ['result/spreaders-0.txt']
	counter = 1
	while counter < 10:
		filenames.append('result/spreaders-' + str(counter) + '.txt')
		with open('tmp/spreaders-' + str(counter) + '.txt', 'w') as outfile:
			for fname in filenames:
				with open(fname) as infile:
					for line in infile:
						outfile.write(line)
		counter += 1

# calculate the average number of distinct hosts in each second
def get_average():
	avg = []
	spreaders = load_data('tmp/spreaders-5.txt', {})
	total = 0
	for spread in spreaders:
		total += spreaders[spread]
	avg.append(total / len(spreaders))
	avg = np.asarray(avg)
	print('Average ', avg.mean())

if __name__ == '__main__':
	#combine_files()
	spreaders = {}
	spreaders = load_data('result/spreaders.txt', spreaders)
	ks, counts, histo = get_count(spreaders)
	plotter(ks, counts, 'threshold k', 'Number of Superspreaders vs the threshold k', histo)
	t, counts = get_count_by_time()
	plotter(t, counts, 'time in seconds', 'Number of 200-Superspreaders vs time interval')
	#get_average()
	print('done')