resource "aws_ecr_repository" "kakfamoon_api" {
  name = "kakfamoon-api"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    "application": "kakfamoon-api"
  }
}
