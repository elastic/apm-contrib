FROM node:12

WORKDIR /app
ADD . /app
RUN npm install
CMD ["node", "-r", "./tracing.js", "app.js"]

