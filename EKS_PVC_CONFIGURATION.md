# Configure Amazon EBS CSI for EKS

To configure the Amazon Elastic Block Store (EBS) Container Storage Interface (CSI) on your Amazon EKS cluster, follow these steps.

Documentation:

- https://docs.aws.amazon.com/eks/latest/userguide/enable-iam-roles-for-service-accounts.html
- https://docs.aws.amazon.com/eks/latest/userguide/csi-iam-role.html
- https://docs.aws.amazon.com/eks/latest/userguide/managing-ebs-csi.html#adding-ebs-csi-eks-add-on

1. Enable the IAM OIDC Provider
First, enable the OpenID Connect (OIDC) provider for your EKS cluster. This allows you to associate IAM roles with Kubernetes service accounts.

```shell
eksctl utils associate-iam-oidc-provider --region=<region> --cluster=<cluster-name> --approve
```

2. Create an IAM Role for the EBS CSI Controller

```shell
eksctl create iamserviceaccount \
    --name ebs-csi-controller-sa \
    --namespace kube-system \
    --cluster <cluster-name> \
    --role-name AmazonEKS_EBS_CSI_DriverRole \
    --role-only \
    --attach-policy-arn arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy \
    --approve
```

3. Add Amazon EBS CSI Add-on

```shell
eksctl create addon --name aws-ebs-csi-driver --cluster <cluster-name> --service-account-role-arn arn:aws:iam::<aws-account>:role/AmazonEKS_EBS_CSI_DriverRole --force
```
