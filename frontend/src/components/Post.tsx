import type { BackendPost } from "../types/post";

function getPostTypeBadgeClass(postType: string) {
  const lower = postType.toLowerCase();

  if (lower.includes("announcement")) {
    return "border-black bg-black text-white";
  }

  if (lower.includes("achievement")) {
    return "border-amber-200 bg-amber-200 text-amber-800";
  }

  if (lower.includes("share")) {
    return "border-green-300 bg-green-500 text-white";
  }

  return "border-red-200 bg-red-100 text-red-700";
}

function formatCreatedAt(value: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Unknown date";

  return date.toLocaleString(undefined, {
    month: "short",
    day: "numeric",
    year: "numeric",
    hour: "numeric",
    minute: "2-digit",
  });
}

function Post({ post }: { post: BackendPost }) {
  return (
    <article
      className={`border-border bg-card w-40 rounded-2xl border-[1.5px] p-5 shadow-[0_2px_10px_rgba(0,0,0,0.04)] transition-all duration-200 hover:shadow-[0_8px_24px_rgba(0,0,0,0.08)] sm:w-48 md:w-64 lg:w-90 xl:w-125`}
    >
      <div className="mb-3 flex items-start justify-between gap-3">
        <div className="min-w-0">
          <p className="font-display text-text-soft text-[0.7rem] font-extrabold uppercase">
            Post #{post.id}
          </p>
          <h3 className="font-display text-charcoal mt-1 text-[1.05rem] leading-tight font-extrabold">
            {post.title}
          </h3>
        </div>
        <span
          className={`font-display shrink-0 rounded-[99px] border px-2.5 py-1 text-[0.60rem] font-extrabold uppercase ${getPostTypeBadgeClass(
            post.postType,
          )}`}
        >
          {post.postType.toUpperCase()}
        </span>
      </div>
      <p className="text-text mb-4 text-sm leading-relaxed">{post.content}</p>
      <div className="border-border flex items-center justify-between border-t pt-3">
        <div className="min-w-0">
          <p className="font-display text-text-soft text-[0.65rem] font-extrabold uppercase">
            Author
          </p>
          <p className="text-charcoal truncate text-xs font-semibold">
            {/*Don't have username implemented yet so just shortened UUID for now */}
            {post.author.substring(0, 8) + "..."}
          </p>
        </div>
        <div className="text-right">
          <p className="font-display text-text-soft text-[0.65rem] font-extrabold uppercase">
            Created
          </p>
          <p className="text-text text-xs font-semibold">
            {formatCreatedAt(post.createdAt)}
          </p>
        </div>
      </div>
    </article>
  );
}

export default Post;
