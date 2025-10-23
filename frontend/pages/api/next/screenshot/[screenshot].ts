// pages/api/check-image.ts
import { NextApiRequest, NextApiResponse } from "next";
import fs from "fs";
import path from "path";
import axios from "axios";

const IMAGE_DIR = getImageDir();
const SERVED_IMAGE =
  process.env.NEXT_PUBLIC_SERVER_URL &&
  process.env.NEXT_PUBLIC_SERVER_URL !== "undefined"
    ? process.env.NEXT_PUBLIC_SERVER_URL + "/api/screenshots/"
    : null;

function getImageDir(): string {
  if (process.env.NEXT_PUBLIC_IMAGE_DIR) {
    return process.env.NEXT_PUBLIC_IMAGE_DIR;
  }
  // use the dev default if not set.
  return "../data/screenshots/";
}

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse,
) {
  const { screenshot } = req.query;

  // Validate screenshot parameter exists and is a string
  if (!screenshot || typeof screenshot !== "string") {
    res.status(400).send("No file");
    return;
  }

  // Prevent path traversal attacks by checking for directory traversal patterns
  if (screenshot.includes("..") || screenshot.includes("/") || screenshot.includes("\\")) {
    res.status(400).send("Invalid filename");
    return;
  }

  const publicPath = path.join(process.cwd(), IMAGE_DIR, screenshot);

  if (fs.existsSync(publicPath)) {
    fs.readFile(publicPath, (err, data) => {
      if (err) {
        res.status(500).json({ error: "Unable to read file" });
        return;
      }
      res.setHeader("Content-Type", "image/png");
      res.setHeader(
        "Content-Disposition",
        `attachment; filename=${screenshot}`,
      );
      res.status(200).send(data);
    });
  } else if (SERVED_IMAGE) {
    const fetched = await axios.get(SERVED_IMAGE + screenshot);
    res.status(200).send(fetched);
  } else {
    res.status(400).send("No file");
  }
}
export const config = {
  api: {
    externalResolver: true,
  },
};
