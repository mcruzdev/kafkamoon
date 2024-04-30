# Configure Amazon EBS CSI for EKS

To configure the Amazon Elastic Block Store (EBS) Container Storage Interface (CSI) on your Amazon EKS cluster, follow these steps.

1. Enable the IAM OIDC Provider
First, enable the OpenID Connect (OIDC) provider for your EKS cluster. This allows you to associate IAM roles with Kubernetes service accounts.

```shell
eksctl utils associate-iam-oidc-provider --region=us-east-1 --cluster=platformoon-kafka --approve
```

2. Create an IAM Role for the EBS CSI Controller

```shell
eksctl create iamserviceaccount \
  --region us-east-1 \
  --name ebs-csi-controller-sa \
  --namespace kube-system \
  --cluster platformoon-kafka \
  --attach-policy-arn arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy \
  --approve \
  --role-only \
  --role-name AmazonEKS_EBS_CSI_DriverRole
```

3. Add Amazon EBS CSI Add-on

```
eksctl create addon --name aws-ebs-csi-driver --cluster platformoon-kafka --service-account-role-arn arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/AmazonEKS_EBS_CSI_DriverRole --force
```
