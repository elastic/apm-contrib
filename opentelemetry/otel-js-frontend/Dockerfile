FROM node:16

WORKDIR /app
ADD . /app
RUN npm install
CMD ["node", "-r", "./tracing.js", "app.js"]

