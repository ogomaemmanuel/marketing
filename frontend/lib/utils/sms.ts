/** SMS helpers: GSM-7/UCS-2 segment maths and Pebble `{{ var }}` variable handling. */

const GSM7_BASIC =
  "@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà";
const GSM7_EXTENDED = "^{}\\[~]|€\f";

export type SmsEncoding = "GSM-7" | "UCS-2";

export interface SmsStats {
  encoding: SmsEncoding;
  /** Length in encoding units (GSM-7 septets or UTF-16 code units). */
  length: number;
  segments: number;
  perSegment: number;
  remaining: number;
}

export function getSmsStats(text: string): SmsStats {
  let septets = 0;
  let isGsm = true;
  for (const char of text) {
    if (GSM7_BASIC.includes(char)) septets += 1;
    else if (GSM7_EXTENDED.includes(char)) septets += 2;
    else {
      isGsm = false;
      break;
    }
  }

  const encoding: SmsEncoding = isGsm ? "GSM-7" : "UCS-2";
  const length = isGsm ? septets : text.length;
  const single = isGsm ? 160 : 70;
  const multi = isGsm ? 153 : 67;

  if (length <= single) {
    return { encoding, length, segments: length === 0 ? 0 : 1, perSegment: single, remaining: single - length };
  }
  const segments = Math.ceil(length / multi);
  return { encoding, length, segments, perSegment: multi, remaining: segments * multi - length };
}

const VARIABLE_PATTERN = /\{\{\s*([A-Za-z_][\w.]*)\s*\}\}/g;

export function extractVariables(text: string): string[] {
  return [...new Set(Array.from(text.matchAll(VARIABLE_PATTERN), (match) => match[1]))];
}

export function renderWithSampleData(text: string, sample: Record<string, string>): string {
  return text.replace(VARIABLE_PATTERN, (token, name: string) => sample[name] ?? token);
}

/** Strips characters Pebble can't use in a variable name; returns "" when nothing valid remains. */
export function sanitizeVariableName(raw: string): string {
  const cleaned = raw.trim().replace(/[^\w.]/g, "").replace(/^[^A-Za-z_]+/, "");
  return cleaned.replace(/\.+$/, "");
}

/** Contact columns the campaign sender passes to the template renderer. */
export const CONTACT_VARIABLES = ["first_name", "last_name", "email", "phone_number"] as const;

export const SAMPLE_VARIABLE_VALUES: Record<string, string> = {
  first_name: "Amina",
  last_name: "Odhiambo",
  email: "amina@example.com",
  phone_number: "+254712345678",
  code: "824193",
  order_id: "ORD-10293",
  amount: "KES 2,450",
  date: "Mon 14 Oct, 10:00",
  link: "https://ogo.ma/x7k2",
  company: "Ogoma",
};
