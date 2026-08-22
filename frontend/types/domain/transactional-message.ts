export type TransactionalChannel = "EMAIL" | "SMS";

interface TransactionalMessageBase {
  scheduledAt?: string;
  templatedId: string;
  params: Record<string, unknown>;
}

export interface EmailTransactionalMessageInput extends TransactionalMessageBase {
  channel: "EMAIL";
  recipients: string[];
}

export interface SmsTransactionalMessageInput extends TransactionalMessageBase {
  channel: "SMS";
  recipient: string;
}

export type TransactionalMessageInput =
  | EmailTransactionalMessageInput
  | SmsTransactionalMessageInput;
