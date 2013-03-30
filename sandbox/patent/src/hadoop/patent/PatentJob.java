package hadoop.patent;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class PatentJob {

	public static class PatentMapper extends Mapper<Object, Text, Text, Text> {
	
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Text k = new Text();
			k.set(key.toString());
			context.write(value, k);
		}
	}
	
	public static class PatentReducer extends Reducer<Text,Text,Text,Text> {
 
		 public void reduce(Text key, Iterable<Text> values, Context context
                 ) throws IOException, InterruptedException {
				String csv = "";
				Iterator<Text> v = values.iterator();
				while (v.hasNext()) {
					if (csv.length() > 0) csv += ",";
    				csv += v.next().toString();
				}
				Text rc = new Text();
				rc.set(csv);
				context.write(key, rc);
			}
	}

	
	public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
		
		Job job = new Job(conf, "Patent Reverse");
		
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	
	    job.setJarByClass(PatentJob.class);
		job.setMapperClass(PatentMapper.class);
	//	job.setCombinerClass(PatentReducer.class);
		job.setReducerClass(PatentReducer.class);
		
//		job.setInputFormatClass(KeyValueTextInputFormat.class);
//		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
	//	job.set("key.value.separator.in.input.line", ",");
		
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
}
