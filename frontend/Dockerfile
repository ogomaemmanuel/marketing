ARG NODE_BASE_IMAGE=559104660845.dkr.ecr.eu-west-1.amazonaws.com/node:24-alpine-patched
FROM ${NODE_BASE_IMAGE} AS builder

WORKDIR /app

# Copy package.json and package-lock.json first for better caching
COPY package.json package-lock.json ./
# Install dependencies
RUN npm ci

# Copy the rest of the application
COPY . .

# Build the Next.js application in standalone mode
RUN npm run build:standalone && npm prune --production

# ----------- Production Image ------------
FROM ${NODE_BASE_IMAGE} AS runner

WORKDIR /app

ENV NODE_ENV=production
ENV PORT=3000
ENV HOSTNAME="0.0.0.0"
# Copy the built application from the builder stage
COPY --from=builder --chown=node:nodejs /app/.next/standalone ./

USER node
# Expose the port Next.js runs on
EXPOSE 3000

# Run the Next.js app
CMD ["node", "server.js"]
