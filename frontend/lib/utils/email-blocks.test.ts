import {describe, expect, it} from "vitest";
import {
    createDefaultBlock,
    duplicateBlock,
    findBlock,
    insertBlock,
    moveBlock,
    removeBlock,
    shiftBlock,
    withBlockIds,
} from "./email-blocks";
import type {EmailBlock, HorizontalLayoutBlock, VerticalLayoutBlock} from "@/types/domain/email-template";

const ids = (blocks: (EmailBlock | null)[]) => blocks.map((block) => block?.id ?? null);

describe("email block tree", () => {
  it("inserts into a column slot and keeps empty slots", () => {
    const columns = createDefaultBlock("horizontal-layout") as HorizontalLayoutBlock;
    const heading = createDefaultBlock("heading");
    const result = insertBlock([columns], { parentId: columns.id!, index: 1 }, heading);
    expect(ids((result[0] as HorizontalLayoutBlock).children!)).toEqual([null, heading.id]);
  });

  it("refuses to drop into an occupied column slot", () => {
    const columns = createDefaultBlock("horizontal-layout") as HorizontalLayoutBlock;
    const a = createDefaultBlock("heading");
    const b = createDefaultBlock("paragraph");
    const blocks = insertBlock([columns, b], { parentId: columns.id!, index: 0 }, a);
    expect(moveBlock(blocks, b.id!, { parentId: columns.id!, index: 0 })).toBe(blocks);
  });

  it("removing from a column leaves the slot empty", () => {
    const columns = createDefaultBlock("horizontal-layout") as HorizontalLayoutBlock;
    const heading = createDefaultBlock("heading");
    const blocks = insertBlock([columns], { parentId: columns.id!, index: 0 }, heading);
    expect(ids((removeBlock(blocks, heading.id!)[0] as HorizontalLayoutBlock).children!)).toEqual([null, null]);
  });

  it("moves a top-level block into a container", () => {
    const container = createDefaultBlock("vertical-layout") as VerticalLayoutBlock;
    const heading = createDefaultBlock("heading");
    const result = moveBlock([heading, container], heading.id!, { parentId: container.id!, index: 0 });
    expect(ids(result)).toEqual([container.id]);
    expect(ids((result[0] as VerticalLayoutBlock).children!)).toEqual([heading.id]);
  });

  it("won't move a container into itself", () => {
    const container = createDefaultBlock("vertical-layout") as VerticalLayoutBlock;
    const blocks = [container];
    expect(moveBlock(blocks, container.id!, { parentId: container.id!, index: 0 })).toBe(blocks);
  });

  it("shifts blocks up and down among siblings", () => {
    const [a, b, c] = ["heading", "paragraph", "button"].map((type) => createDefaultBlock(type as EmailBlock["type"]));
    expect(ids(shiftBlock([a, b, c], a.id!, 1))).toEqual([b.id, a.id, c.id]);
    expect(ids(shiftBlock([a, b, c], c.id!, -1))).toEqual([a.id, c.id, b.id]);
    expect(ids(shiftBlock([a, b, c], a.id!, -1))).toEqual([a.id, b.id, c.id]);
  });

  it("duplicates nested blocks with fresh ids", () => {
    const container = createDefaultBlock("vertical-layout") as VerticalLayoutBlock;
    const heading = createDefaultBlock("heading");
    const blocks = insertBlock([container], { parentId: container.id!, index: 0 }, heading);
    const { blocks: result, newId } = duplicateBlock(blocks, container.id!);
    expect(result).toHaveLength(2);
    const copy = findBlock(result, newId!) as VerticalLayoutBlock;
    expect(copy.children![0].id).not.toBe(heading.id);
  });

  it("assigns ids to loaded blocks recursively", () => {
    const loaded = [{ type: "vertical-layout", children: [{ type: "heading", level: 2 }] }] as EmailBlock[];
    const [container] = withBlockIds(loaded) as VerticalLayoutBlock[];
    expect(container.id).toBeTruthy();
    expect(container.children![0].id).toBeTruthy();
  });
});
