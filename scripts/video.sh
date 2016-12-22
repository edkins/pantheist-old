filename=dump/video-`date +%Y-%m-%d--%H-%M-%S`.mp4
ffmpeg -f x11grab -video_size 1280x1024 -i :99 "$filename"
