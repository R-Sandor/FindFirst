// pages/api/check-image.ts
import { NextApiRequest, NextApiResponse } from "next";
import fs from "fs";
import path from "path";
import axios from "axios";

const IMAGE_DIR = process.env.NEXT_PUBLIC_IMAGE_DIR;
const SERVED_IMAGE = process.env.NEXT_PUBLIC_SERVER_URL + "/api/screenshots/";
export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse,
) {
  const { screenshot } = req.query;

  const publicPath = path.join(
    process.cwd(),
    IMAGE_DIR ? IMAGE_DIR : "../data/screenshots/",
    screenshot as string,
  );
  console.log(publicPath);

  if (fs.existsSync(publicPath) && screenshot) {
    fs.readFile(publicPath, (err, data) => {
      if (err) {
        res.status(500).json({ error: "Unable to read file" });
      }
      res.setHeader("Content-Type", "image/png");
      res.setHeader(
        "Content-Disposition",
        `attachment; filename=${screenshot}`,
      );
      res.status(200).send(data);
    });
  } else if (screenshot && SERVED_IMAGE) {
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
