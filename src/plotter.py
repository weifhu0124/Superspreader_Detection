from matplotlib import pyplot as plt

def plotter():
	threshold = [5,10,15,20,25]
	recall = [0.6770833333333334,0.6666666666666666,0.6666666666666666,0.65625,0.6458333333333334]
	precision = [0.9420289855072463,0.9411764705882353,1,1,1]

	plt.plot(threshold, recall, label='recall')
	plt.plot(threshold, precision, label='precision')
	plt.xlabel('k threshold for superspreader')
	plt.ylabel('value')
	plt.legend()
	plt.savefig('precision-recall.png')

if __name__ == '__main__':
	plotter()