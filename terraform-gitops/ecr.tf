resource "aws_ecr_repository" "kakfamoon_api" {
  name = "kafkamoon-api"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    "application": "kafkamoon-api"
  }
}
