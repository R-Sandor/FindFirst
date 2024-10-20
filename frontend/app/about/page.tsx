"use client";
import styles from "./about.module.scss";

export default function Page() {
  return (
    <div className={` ${styles.center} grid`}>
      <div className={styles.about_box}>
        <h1 className="display-6 mb-3">About Us</h1>
        <p>
          Welcome to our platform! We are dedicated to providing the best
          services to our users. Our team is focused on creating innovative
          solutions to help you succeed.
        </p>
        <p>
          Our mission is to deliver high-quality products that meet the needs of
          our community. We value feedback and strive for continuous
          improvement.
        </p>
        <p>
          Thank you for choosing us, and we look forward to working together!
        </p>
      </div>
    </div>
  );
}