import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Evaluate {
    // ground truth label
    private Set<Long> target;
    // predicted value
    private Set<Long> predicted;
    // false positive
    public int fp;
    // true positive
    public int tp;
    // false negative
    public int fn;


    // constructor
    public Evaluate(ArrayList<SourceWithCount> target, ArrayList<SourceWithCount> predicted) {
        this.target = new HashSet<Long>();
        this.predicted = new HashSet<Long>();
        Set<SourceWithCount> target_set = new HashSet<SourceWithCount>(target);
        for (SourceWithCount s : target_set){
            this.target.add(s.sourceId);
        }
        Set<SourceWithCount> predicted_set = new HashSet<SourceWithCount>(predicted);
        for (SourceWithCount s : predicted_set){
            this.predicted.add(s.sourceId);
        }
        fp = 0;
        tp = 0;
        fn = 0;
        setFp();
        setTp();
        setFn();
    }

    // compute false positive
    // number of elements in set predicted but not in label
    private void setFp(){
        for (Long sourceIp : predicted){
            if (!target.contains(sourceIp)){
                fp += 1;
            }
        }
    }

    // compute true positive
    // number of elements in set predicted and in label
    private void setTp(){
        for (Long sourceIp : predicted){
            if (target.contains(sourceIp)){
                tp += 1;
            }
        }
    }

    // compute false negatives
    // number of elements in label but not in predicted
    private void setFn(){
        for (Long sourceIp : target){
            if (!predicted.contains(sourceIp)){
                fn += 1;
            }
        }
    }

    // compute accuracy as correct_prediction / total
    public double accuracy(){
        double correct = 0;
        for (Long sourceIp: predicted){
            if (target.contains(sourceIp)){
                correct += 1;
            }
        }
        return correct / target.size();
    }

    // compute precision
    public double precision(){
        return (double)tp / (tp + fp);
    }

    // compute recall
    public double recall(){
        return (double)tp / (tp + fn);
    }

}
