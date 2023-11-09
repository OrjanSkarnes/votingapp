# Docker file for cloud deployment of the frontend
FROM node:18

WORKDIR /frontend

COPY package.json package-lock.json ./
RUN npm ci

COPY . /frontend
RUN npm run build

EXPOSE 3000

CMD ["npm", "run", "start"]