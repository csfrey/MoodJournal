FROM node:20-alpine3.20 as builder
WORKDIR /app
COPY . .
RUN rm -rf node_modules && yarn
RUN yarn run build

FROM nginx:1.21.5-alpine
COPY --chown=nginx:nginx nginx-ui.conf /etc/nginx/conf.d/default.conf
COPY --chown=nginx:nginx --from=builder /app/build /var/www/html/