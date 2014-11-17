package com.sciul.cloud_configurator;


/**
 * Created by sumeetrohatgi on 11/14/14.
 */
// @Component
// public class AwsProvider implements Provider {
// private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
// private static final String SECRET_KEY = "AWS_SECRET_KEY";
// private static Logger logger = LoggerFactory.getLogger(AwsProvider.class);
//
// private AWSCredentials credentials;
// private AmazonCloudFormationClient clt;
// private String region;
//
// public AwsProvider() {
// try {
// credentials = new ProfileCredentialsProvider().getCredentials();
// clt = new AmazonCloudFormationClient(credentials);
// } catch (Exception e) {
// throw new RuntimeException("Cannot load the credentials from the credential profiles file. "
// + "Please make sure that your credentials file is at the correct "
// + "location (~/.aws/credentials), and is in valid format.", e);
// }
// }
//
// public String getRegion() {
// return region;
// }
//
// public void setRegion(String region) {
// if (region == null) {
// throw new RuntimeException("illegal region value: " + region);
// }
// this.region = region;
// Regions r = Regions.DEFAULT_REGION;
// switch (region) {
// case "us-west-2":
// r = Regions.US_WEST_2;
// break;
// default:
// throw new RuntimeException("illegal region: " + region);
// }
// clt.setRegion(Region.getRegion(Regions.US_WEST_2));
// }
//
// public CreateStackResult createStack(final String environment) {
// if (environment == null || environment.length() == 0) {
// throw new RuntimeException("illegal environment name: " + environment);
// }
// try {
// final String template =
// convertStreamToString(AwsProvider.class.getResourceAsStream("/templates/cf-template-1.json"));
// CreateStackRequest crq = new CreateStackRequest() {
// {
// setStackName(environment);
// setTemplateBody(template);
// }
// };
//
// logger.debug("creating a new stack named: {}", environment);
// CreateStackResult crs = clt.createStack(crq);
//
// logger.debug("stack create result: {}", crs);
// return crs;
// } catch (AmazonServiceException ae) {
// throw new RuntimeException("server error", ae);
// } catch (AmazonClientException ae) {
// throw new RuntimeException("client error", ae);
// }
// }
//
// public DescribeStacksResult describeStacks(String environment) {
// // logger.debug(s.getVpcName());
// DescribeStacksRequest d = new DescribeStacksRequest();
// if (environment != null && !environment.equalsIgnoreCase("")) {
// d.setStackName(environment);
// }
// return clt.describeStacks(d);
// }
//
// public ListStacksResult listStacks(String[] args) {
// ListStacksRequest rq = new ListStacksRequest();
// rq.setStackStatusFilters(new ArrayList<String>() {
// {
// add("CREATE_COMPLETE");
// }
// });
// ListStacksResult lst = clt.listStacks(rq);
// return lst;
// }
//
// public static String convertStreamToString(InputStream in) {
// BufferedReader reader = new BufferedReader(new InputStreamReader(in));
// StringBuilder stringbuilder = new StringBuilder();
// String line = null;
// try {
// while ((line = reader.readLine()) != null) {
// stringbuilder.append(line + "\n");
// }
// in.close();
// } catch (IOException e) {
// throw new RuntimeException("unable to read file!", e);
// }
// return stringbuilder.toString();
// }
//
// public void processJson(String[] args) {
// if (args.length != 1) {
// throw new RuntimeException("Usage: processJson <path to file>");
// }
//
// String path = args[0];
//
// CreateStackRequest crt = new CreateStackRequest();
// String template = "";
// try {
// List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
// StringBuffer sbf = new StringBuffer();
// for (String l : lines) {
// sbf.append(l);
// }
// template = sbf.toString();
// if (template.length() == 0) {
// throw new RuntimeException("zero length file read from path: " + path);
// }
// } catch (IOException e) {
// throw new RuntimeException("Unable to read file from path: " + path, e);
// }
// crt.setTemplateBody("");
// }
//
// }
